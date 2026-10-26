package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = RosePrimary,
    onPrimary = Color.White,
    primaryContainer = RoseContainer,
    onPrimaryContainer = OnRoseContainer,
    secondary = SunsetPeach,
    onSecondary = Color(0xFF3B1509),
    secondaryContainer = PeachContainer,
    onSecondaryContainer = OnPeachContainer,
    tertiary = GoldChampagne,
    onTertiary = Color(0xFF3E2D00),
    tertiaryContainer = GoldContainer,
    onTertiaryContainer = OnGoldContainer,
    background = NightBackground,
    onBackground = TextPrimary,
    surface = NightSurface,
    onSurface = TextPrimary,
    surfaceVariant = NightSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = OutlineSubtle
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFD81B60),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFD8E4),
    onPrimaryContainer = Color(0xFF3D001D),
    secondary = Color(0xFF8A5100),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFDCC1),
    onSecondaryContainer = Color(0xFF2E1500),
    tertiary = Color(0xFF745B00),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFE088),
    onTertiaryContainer = Color(0xFF241A00),
    background = Color(0xFFFFF8F8),
    onBackground = Color(0xFF22191C),
    surface = Color(0xFFFFF0F3),
    onSurface = Color(0xFF22191C),
    surfaceVariant = Color(0xFFF3DDE3),
    onSurfaceVariant = Color(0xFF524348),
    outline = Color(0xFF847378)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to stunning dark romantic theme
    dynamicColor: Boolean = false, // Keep signature romantic aesthetic
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
