package com.mhksoft.smilinno.ui.theme

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = primary80,
    onPrimary = primary20,
    primaryContainer = primary30,
    onPrimaryContainer = primary90,
    inversePrimary = primary40,
    secondary = secondary80,
    onSecondary = secondary20,
    secondaryContainer = secondary30,
    onSecondaryContainer = secondary90,
    tertiary = tertiary80,
    onTertiary = tertiary20,
    tertiaryContainer = tertiary30,
    onTertiaryContainer = tertiary90,
    error = error80,
    onError = error20,
    errorContainer = error30,
    onErrorContainer = error90,
    background = neutral0,
    onBackground = neutral100,
    surface = neutral10,
    onSurface = neutral100,
    inverseSurface = neutral90,
    inverseOnSurface = neutral20,
    surfaceVariant = neutral30,
    onSurfaceVariant = neutral80,
    outline = neutral60
)

private val LightColorScheme = lightColorScheme(
    primary = primary40,
    onPrimary = Color.White,
    primaryContainer = primary90,
    onPrimaryContainer = primary10,
    inversePrimary = primary80,
    secondary = secondary40,
    onSecondary = Color.White,
    secondaryContainer = secondary90,
    onSecondaryContainer = secondary10,
    tertiary = tertiary40,
    onTertiary = Color.White,
    tertiaryContainer = tertiary90,
    onTertiaryContainer = tertiary10,
    error = error40,
    onError = Color.White,
    errorContainer = error90,
    onErrorContainer = error10,
    background = neutral99,
    onBackground = neutral0,
    surface = neutral100,
    onSurface = neutral0,
    inverseSurface = neutral20,
    inverseOnSurface = neutral95,
    surfaceVariant = neutral90,
    onSurfaceVariant = neutral30,
    outline = neutral50
)

enum class UiMode {
    Default,
    Dark;

    fun toggle(): UiMode = when (this) {
        Default -> Dark
        Dark -> Default
    }
}

val LocalUiMode = staticCompositionLocalOf<MutableState<UiMode>> {
    error("UiMode not provided")
}

@SuppressLint("NewApi")
@Composable
fun SmilinnoTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    isDynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val uiMode = remember {
        mutableStateOf(if (isDarkTheme) UiMode.Dark else UiMode.Default)
    }
    val dynamicColor = isDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    CompositionLocalProvider(LocalUiMode provides uiMode) {
        val smilinnoColorScheme = when {
            LocalUiMode.current.value == UiMode.Dark && dynamicColor -> {
                dynamicDarkColorScheme(LocalContext.current)
            }

            LocalUiMode.current.value == UiMode.Default && dynamicColor -> {
                dynamicLightColorScheme(LocalContext.current)
            }

            LocalUiMode.current.value == UiMode.Dark -> DarkColorScheme

            else -> LightColorScheme
        }

        MaterialTheme(
            colorScheme = animate(smilinnoColorScheme),
            typography = SmilinnoTypography,
            content = content
        )
    }
}

@Composable
private fun animate(colorScheme: ColorScheme): ColorScheme {
    val animSpec = remember {
        spring<Color>(stiffness = 500f)
    }

    @Composable
    fun animateColor(color: Color): Color =
        animateColorAsState(targetValue = color, animationSpec = animSpec).value

    return ColorScheme(
        primary = animateColor(colorScheme.primary),
        onPrimary = animateColor(colorScheme.onPrimary),
        primaryContainer = animateColor(colorScheme.primaryContainer),
        onPrimaryContainer = animateColor(colorScheme.onPrimaryContainer),
        inversePrimary = animateColor(colorScheme.inversePrimary),
        secondary = animateColor(colorScheme.secondary),
        onSecondary = animateColor(colorScheme.onSecondary),
        secondaryContainer = animateColor(colorScheme.secondaryContainer),
        onSecondaryContainer = animateColor(colorScheme.onSecondaryContainer),
        tertiary = animateColor(colorScheme.tertiary),
        onTertiary = animateColor(colorScheme.onTertiary),
        tertiaryContainer = animateColor(colorScheme.tertiaryContainer),
        onTertiaryContainer = animateColor(colorScheme.onTertiaryContainer),
        error = animateColor(colorScheme.error),
        onError = animateColor(colorScheme.onError),
        errorContainer = animateColor(colorScheme.errorContainer),
        onErrorContainer = animateColor(colorScheme.onErrorContainer),
        background = animateColor(colorScheme.background),
        onBackground = animateColor(colorScheme.onBackground),
        surface = animateColor(colorScheme.surface),
        onSurface = animateColor(colorScheme.onSurface),
        surfaceTint = animateColor(colorScheme.surfaceTint),
        inverseSurface = animateColor(colorScheme.inverseSurface),
        inverseOnSurface = animateColor(colorScheme.inverseOnSurface),
        surfaceVariant = animateColor(colorScheme.surfaceVariant),
        onSurfaceVariant = animateColor(colorScheme.onSurfaceVariant),
        outline = animateColor(colorScheme.outline),
        outlineVariant = animateColor(colorScheme.outlineVariant),
        scrim = animateColor(colorScheme.scrim),
    )
}