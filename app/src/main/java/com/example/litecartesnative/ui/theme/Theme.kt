package com.example.litecartesnative.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Custom color scheme using the app's warm orange/brown theme
private val LitecartesColorScheme = lightColorScheme(
    primary = LitecartesColor.Primary,
    onPrimary = Color.White,
    primaryContainer = LitecartesColor.DarkerSurface,
    onPrimaryContainer = LitecartesColor.DarkBrown,
    secondary = LitecartesColor.Secondary,
    onSecondary = Color.White,
    secondaryContainer = LitecartesColor.DarkerSurface,
    onSecondaryContainer = LitecartesColor.DarkBrown,
    tertiary = LitecartesColor.Tertiary,
    onTertiary = Color.White,
    background = LitecartesColor.Surface,
    onBackground = LitecartesColor.DarkBrown,
    surface = LitecartesColor.Surface,
    onSurface = LitecartesColor.DarkBrown,
    surfaceVariant = LitecartesColor.DarkerSurface,
    onSurfaceVariant = LitecartesColor.Secondary,
    outline = LitecartesColor.Secondary,
    outlineVariant = LitecartesColor.Secondary.copy(alpha = 0.5f)
)

@Composable
fun LitecartesNativeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Disable dynamic color to use our custom theme
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // Always use our custom color scheme
    val colorScheme = LitecartesColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Set status bar color to match the surface
            window.statusBarColor = LitecartesColor.Surface.toArgb()
            // Use dark icons on light background
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
