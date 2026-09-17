package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class AppThemeMode {
    SYSTEM, LIGHT, DARK
}

private val DarkColorScheme = darkColorScheme(
    primary = NatnaelPrimaryIndigo,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF312E81),
    onPrimaryContainer = Color(0xFFE0E7FF),
    secondary = NatnaelPrimaryCyan,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF164E63),
    onSecondaryContainer = Color(0xFFCFFAFE),
    tertiary = NatnaelAccentGold,
    onTertiary = Color.Black,
    background = NatnaelDarkBg,
    onBackground = NatnaelDarkTextPrimary,
    surface = NatnaelDarkSurface,
    onSurface = NatnaelDarkTextPrimary,
    surfaceVariant = NatnaelDarkSurfaceVariant,
    onSurfaceVariant = NatnaelDarkTextSecondary,
    outline = NatnaelDarkBorder,
    outlineVariant = Color(0xFF1E293B)
)

private val LightColorScheme = lightColorScheme(
    primary = NatnaelPrimaryIndigo,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEEF2FF),
    onPrimaryContainer = Color(0xFF312E81),
    secondary = NatnaelPrimaryCyan,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE0F2FE),
    onSecondaryContainer = Color(0xFF0369A1),
    tertiary = NatnaelAccentGold,
    onTertiary = Color.Black,
    background = NatnaelLightBg,
    onBackground = NatnaelLightTextPrimary,
    surface = NatnaelLightSurface,
    onSurface = NatnaelLightTextPrimary,
    surfaceVariant = NatnaelLightSurfaceVariant,
    onSurfaceVariant = NatnaelLightTextSecondary,
    outline = NatnaelLightBorder,
    outlineVariant = Color(0xFFCBD5E1)
)

@Composable
fun NatnaelAITheme(
    themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        AppThemeMode.SYSTEM -> isSystemInDarkTheme()
        AppThemeMode.LIGHT -> false
        AppThemeMode.DARK -> true
    }

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
