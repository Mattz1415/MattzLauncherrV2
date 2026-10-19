package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalLauncherAccent = staticCompositionLocalOf { AccentThemeOption.CYBER_CYAN }

@Composable
fun MattzLauncherTheme(
    accentOption: AccentThemeOption = AccentThemeOption.CYBER_CYAN,
    content: @Composable () -> Unit
) {
    val colorScheme = darkColorScheme(
        primary = accentOption.primaryColor,
        onPrimary = DarkInk,
        secondary = AmberGold,
        onSecondary = DarkInk,
        tertiary = EmeraldGreen,
        background = OledBlack,
        onBackground = TextPrimary,
        surface = GlassSurfaceTop,
        onSurface = TextPrimary,
        surfaceVariant = GlassSurfaceBottom,
        onSurfaceVariant = TextSecondary,
        error = CrimsonRed
    )

    CompositionLocalProvider(LocalLauncherAccent provides accentOption) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MattzLauncherTheme(content = content)
}
