/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mhksoft.smilinno

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mhksoft.smilinno.ui.blog.BlogDetailScreen
import com.mhksoft.smilinno.ui.blog.BlogDetailViewModel
import com.mhksoft.smilinno.ui.home.HomeScreen
import com.mhksoft.smilinno.ui.home.HomeViewModel
import kotlinx.coroutines.CoroutineScope

/**
 * Destinations used in the ([SmilinnoApplication]).
 */

object Destinations {
    const val HOME_ROUTE = "home"
    const val BLOG_DETAIL_ROUTE = "blog_detail"
}

/**
 * Main NavGraph for application
 */
@Composable
fun SmilinnoNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Destinations.HOME_ROUTE,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    val actions = remember(navController) { MainActions(navController) }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            Destinations.HOME_ROUTE,
        ) {
            HomeScreen(
                viewModel = hiltViewModel<HomeViewModel>(),
                navigateToBlogDetail = actions.navigateToBlogDetail,
            )
        }
        composable(
            "${Destinations.BLOG_DETAIL_ROUTE}/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.LongType
            })
        ) {
            BlogDetailScreen(
                viewModel = hiltViewModel<BlogDetailViewModel>(),
                navigateUp = actions.upPress,
                blogId = it.arguments?.getLong("id"),
            )
        }
    }
}

/**
 * Models the navigation actions in the app.
 */
class MainActions(navController: NavHostController) {
    val navigateToBlogDetail: (id: Long) -> Unit = { id: Long ->
        navController.navigate("${Destinations.BLOG_DETAIL_ROUTE}/$id")
    }

    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}