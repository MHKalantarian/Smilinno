package com.mhksoft.smilinno.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mhksoft.smilinno.R
import com.mhksoft.smilinno.SmilinnoNavGraph
import com.mhksoft.smilinno.ui.component.DarkToggleButton
import com.mhksoft.smilinno.ui.theme.LocalUiMode
import com.mhksoft.smilinno.ui.theme.SmilinnoTheme
import com.mhksoft.smilinno.ui.theme.UiMode
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)

        setContent {
            SmilinnoTheme {
                val systemUiController = rememberSystemUiController()
                val uiMode by LocalUiMode.current
                SideEffect {
                    when (uiMode) {
                        UiMode.Default -> systemUiController.setSystemBarsColor(
                            Color.White,
                            darkIcons = true
                        ) {
                            Color.Black
                        }
                        UiMode.Dark ->
                            systemUiController.setSystemBarsColor(
                                Color.Black,
                                darkIcons = false
                            )
                    }
                }

                val navController = rememberNavController()
                val coroutineScope = rememberCoroutineScope()

                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text(text = stringResource(id = R.string.app_name)) },
                            actions = {
                                DarkToggleButton(
                                    modifier = Modifier.padding(8.dp),
                                    springSpec = spring(dampingRatio = 0.5f, stiffness = 100f)
                                )
                            })
                    }
                ) {
                    Box(modifier = Modifier
                        .padding(it)
                        .padding(16.dp)) {
                        SmilinnoNavGraph(
                            navController = navController,
                            coroutineScope = coroutineScope
                        )
                    }
                }
            }
        }
    }
}