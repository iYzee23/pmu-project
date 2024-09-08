package com.example.rad.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

val LightColors = lightColorScheme(
    primary = Color(0xFF6200EA),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFBB86FC),
    onPrimaryContainer = Color(0xFF3700B3),
    inversePrimary = Color(0xFF3700B3),
    secondary = Color(0xFF03DAC5),
    onSecondary = Color(0xFF000000),
    secondaryContainer = Color(0xFF018786),
    onSecondaryContainer = Color(0xFF000000),
    tertiary = Color(0xFF018786),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFF018786),
    onTertiaryContainer = Color(0xFFFFFFFF),
    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF000000),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF000000),
    surfaceVariant = Color(0xFF6200EA),
    onSurfaceVariant = Color(0xFF3700B3),
    surfaceTint = Color(0xFF6200EA),
    inverseSurface = Color(0xFF3700B3),
    inverseOnSurface = Color(0xFFBB86FC),
    error = Color(0xFFB00020),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD4),
    onErrorContainer = Color(0xFF3700B3),
    outline = Color(0xFF6200EA),
    outlineVariant = Color(0xFF3700B3),
    scrim = Color(0xFF000000)
)

val DarkColors = darkColorScheme(
    primary = Color(0xFFBB86FC),
    onPrimary = Color(0xFF000000),
    primaryContainer = Color(0xFF3700B3),
    onPrimaryContainer = Color(0xFF6200EA),
    inversePrimary = Color(0xFF6200EA),
    secondary = Color(0xFF03DAC5),
    onSecondary = Color(0xFF000000),
    secondaryContainer = Color(0xFF018786),
    onSecondaryContainer = Color(0xFF000000),
    tertiary = Color(0xFF03DAC5),
    onTertiary = Color(0xFF000000),
    tertiaryContainer = Color(0xFF018786),
    onTertiaryContainer = Color(0xFF000000),
    background = Color(0xFF121212),
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF121212),
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFBB86FC),
    onSurfaceVariant = Color(0xFF6200EA),
    surfaceTint = Color(0xFFBB86FC),
    inverseSurface = Color(0xFF6200EA),
    inverseOnSurface = Color(0xFF3700B3),
    error = Color(0xFFCF6679),
    onError = Color(0xFF000000),
    errorContainer = Color(0xFF3700B3),
    onErrorContainer = Color(0xFFBB86FC),
    outline = Color(0xFFBB86FC),
    outlineVariant = Color(0xFF6200EA),
    scrim = Color(0xFF000000)
)

@Composable
fun RadTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    // val colorScheme = if (darkTheme) DarkColors else LightColors
    val colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes(),
        content = content
    )
}