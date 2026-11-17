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

private val LightColorScheme = lightColorScheme(
    primary = ClinicalTealPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD0F0FA),
    onPrimaryContainer = ClinicalTealDeep,
    secondary = ClinicalMintGlow,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCCFBF1),
    onSecondaryContainer = Color(0xFF042F2E),
    tertiary = MedGemmaIndigo,
    onTertiary = Color.White,
    tertiaryContainer = MedGemmaContainer,
    onTertiaryContainer = Color(0xFF1E1B4B),
    background = ClinicalBgLight,
    onBackground = Color(0xFF0F172A),
    surface = ClinicalSurfaceLight,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = ClinicalSurfaceVariantLight,
    onSurfaceVariant = Color(0xFF334155),
    outline = ClinicalOutlineLight
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF38BDF8),
    onPrimary = Color(0xFF082F49),
    primaryContainer = ClinicalTealDeep,
    onPrimaryContainer = Color(0xFFE0F2FE),
    secondary = Color(0xFF2DD4BF),
    onSecondary = Color(0xFF042F2E),
    secondaryContainer = Color(0xFF134E4A),
    onSecondaryContainer = Color(0xFFCCFBF1),
    tertiary = Color(0xFF818CF8),
    onTertiary = Color(0xFF1E1B4B),
    tertiaryContainer = Color(0xFF312E81),
    onTertiaryContainer = Color(0xFFE0E7FF),
    background = ClinicalBgDark,
    onBackground = Color(0xFFF1F5F9),
    surface = ClinicalSurfaceDark,
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = ClinicalSurfaceVariantDark,
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = ClinicalOutlineDark
)

@Composable
fun MedicalScoreProTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MedicalTypography,
        content = content
    )
}
