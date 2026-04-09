package com.example.final_project.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary          = Primary,
    secondary        = Secondary,
    background       = Background,
    surface          = Surface,
    surfaceVariant   = SurfaceVariant,
    onPrimary        = Background,
    onSecondary      = Background,
    onBackground     = TextPrimary,
    onSurface        = TextPrimary,
    onSurfaceVariant = TextSecondary,
    error            = ErrorRed,
    outline          = Border,
    outlineVariant   = BorderLight,
)

private val LightColorScheme = lightColorScheme(
    primary      = Primary,
    secondary    = Secondary,
    background   = Color(0xFFF5F7FA),
    surface      = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFEEF1F6),
    onPrimary    = Color.White,
    onBackground = Color(0xFF0F1320),
    onSurface    = Color(0xFF0F1320),
    onSurfaceVariant = Color(0xFF4A5573),
    error        = ErrorRed,
    outline      = Color(0xFFD0D7E3),
    outlineVariant = Color(0xFFE8EDF5),
)

@Composable
fun ViSLTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = ViSLTypography,
        content     = content,
    )
}