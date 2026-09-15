package com.gabriel.eventify.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val EventifyDarkColorScheme = darkColorScheme(
    primary = EventifyPink,
    secondary = EventifyOrange,
    tertiary = EventifyPurple,
    background = EventifyBackground,
    surface = EventifySurface,
    surfaceVariant = EventifySurfaceVariant,
    onPrimary = EventifyTextPrimary,
    onBackground = EventifyTextPrimary,
    onSurface = EventifyTextPrimary,
    onSurfaceVariant = EventifyTextSecondary
)

@Composable
fun EventifyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = EventifyDarkColorScheme,
        typography = EventifyTypography,
        content = content
    )
}
