package com.sunsafe.app.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ─── UV-Scale Palette ──────────────────────────────────────────────────────
object SunSafeColors {
    val UvLow      = Color(0xFF4CAF50)
    val UvModerate = Color(0xFFFFC107)
    val UvHigh     = Color(0xFFFF9800)
    val UvVeryHigh = Color(0xFFF44336)
    val UvExtreme  = Color(0xFF9C27B0)

    val SunOrange      = Color(0xFFFF6D00)
    val SunOrangeLight = Color(0xFFFF9E40)
    val SunOrangeDark  = Color(0xFFE65100)
    val SkyBlue        = Color(0xFF0288D1)
    val SkyBlueLight   = Color(0xFF5EB8FF)
    val SkyBlueDark    = Color(0xFF005B9F)

    val SkinTypeI   = Color(0xFFFFE0B2)
    val SkinTypeII  = Color(0xFFFFCC80)
    val SkinTypeIII = Color(0xFFFFB74D)
    val SkinTypeIV  = Color(0xFFA1887F)
    val SkinTypeV   = Color(0xFF8D6E63)
    val SkinTypeVI  = Color(0xFF5D4037)

    val SafeGreen   = Color(0xFF2E7D32)
    val WarningAmber= Color(0xFFF57F17)
    val DangerRed   = Color(0xFFC62828)

    val BackgroundLight = Color(0xFFFFF8F0)
    val BackgroundDark  = Color(0xFF1A1410)
    val SurfaceDark     = Color(0xFF2D2320)
}

private val LightColorScheme = lightColorScheme(
    primary            = SunSafeColors.SunOrange,
    onPrimary          = Color.White,
    primaryContainer   = Color(0xFFFFDDC1),
    onPrimaryContainer = Color(0xFF341100),
    secondary              = SunSafeColors.SkyBlue,
    onSecondary            = Color.White,
    secondaryContainer     = Color(0xFFCDE5FF),
    onSecondaryContainer   = Color(0xFF001D33),
    tertiary               = Color(0xFF5E6A00),
    onTertiary             = Color.White,
    background             = SunSafeColors.BackgroundLight,
    onBackground           = Color(0xFF201A16),
    surface                = Color.White,
    onSurface              = Color(0xFF201A16),
    surfaceVariant         = Color(0xFFF4DED3),
    onSurfaceVariant       = Color(0xFF52443C),
    outline                = Color(0xFF85736A),
)

private val DarkColorScheme = darkColorScheme(
    primary            = SunSafeColors.SunOrangeLight,
    onPrimary          = Color(0xFF5A1E00),
    primaryContainer   = SunSafeColors.SunOrangeDark,
    onPrimaryContainer = Color(0xFFFFDDC1),
    secondary              = SunSafeColors.SkyBlueLight,
    onSecondary            = Color(0xFF003354),
    secondaryContainer     = SunSafeColors.SkyBlueDark,
    onSecondaryContainer   = Color(0xFFCDE5FF),
    background             = SunSafeColors.BackgroundDark,
    onBackground           = Color(0xFFEDE0D9),
    surface                = SunSafeColors.SurfaceDark,
    onSurface              = Color(0xFFEDE0D9),
    surfaceVariant         = Color(0xFF52443C),
    onSurfaceVariant       = Color(0xFFD7C2B9),
    outline                = Color(0xFFA08D84),
)

val SunSafeTypography = Typography(
    displayLarge  = TextStyle(fontWeight = FontWeight.Bold,   fontSize = 57.sp),
    displayMedium = TextStyle(fontWeight = FontWeight.Bold,   fontSize = 45.sp),
    headlineLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 32.sp),
    headlineMedium= TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 28.sp),
    titleLarge    = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 22.sp),
    titleMedium   = TextStyle(fontWeight = FontWeight.Medium, fontSize = 16.sp),
    bodyLarge     = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp),
    bodyMedium    = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp),
    labelLarge    = TextStyle(fontWeight = FontWeight.Medium, fontSize = 14.sp),
    labelMedium   = TextStyle(fontWeight = FontWeight.Medium, fontSize = 12.sp),
)

@Composable
fun SunSafeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography  = SunSafeTypography,
        content     = content
    )
}

fun uvIndexColor(uv: Double): Color = when {
    uv >= 11.0 -> SunSafeColors.UvExtreme
    uv >= 8.0  -> SunSafeColors.UvVeryHigh
    uv >= 6.0  -> SunSafeColors.UvHigh
    uv >= 3.0  -> SunSafeColors.UvModerate
    else       -> SunSafeColors.UvLow
}

fun exposureStatusColor(pct: Float): Color = when {
    pct >= 100f -> SunSafeColors.UvExtreme
    pct >= 95f  -> SunSafeColors.UvVeryHigh
    pct >= 80f  -> SunSafeColors.UvHigh
    else        -> SunSafeColors.UvLow
}

fun skinTypeColor(ordinal: Int): Color = when (ordinal) {
    0 -> SunSafeColors.SkinTypeI
    1 -> SunSafeColors.SkinTypeII
    2 -> SunSafeColors.SkinTypeIII
    3 -> SunSafeColors.SkinTypeIV
    4 -> SunSafeColors.SkinTypeV
    5 -> SunSafeColors.SkinTypeVI
    else -> SunSafeColors.SkinTypeIII
}
