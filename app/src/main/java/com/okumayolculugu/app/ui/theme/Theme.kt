package com.okumayolculugu.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF7357E8),
    secondary = Color(0xFFFF8FA3),
    background = Color(0xFFFFF9F5),
    surface = Color.White,
    onPrimary = Color.White,
    onBackground = Color(0xFF302936),
    onSurface = Color(0xFF302936)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFB7A6FF),
    secondary = Color(0xFFFFB5C2),
    background = Color(0xFF201C24),
    surface = Color(0xFF2B2630)
)

@Composable
fun OkumaYolculuguTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors, content = content)
}
