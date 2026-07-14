package com.example.io_motion.core.ui.theme

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import com.example.io_motion.core.common.models.AccentTheme
import com.example.io_motion.core.common.models.ThemeMode

// Muted, opaque tint tones derived from each theme's background — used for M3 "container" roles
// (badges, secondary cards) that the design doc doesn't define tokens for but older screens
// (LiveScreen/VideoScreen, outside this redesign's scope) still rely on for visual consistency.
private fun containerTone(background: Color, tint: Color, fraction: Float = 0.18f): Color =
    lerp(background, tint, fraction)

private fun lightColorSchemeFor(accent: Color, accentOn: Color): ColorScheme = lightColorScheme(
    primary = accent,
    onPrimary = accentOn,
    primaryContainer = containerTone(BackgroundLight, accent),
    onPrimaryContainer = TextPrimaryLight,
    secondary = TextMutedLight,
    onSecondary = BackgroundLight,
    secondaryContainer = containerTone(BackgroundLight, TextMutedLight),
    onSecondaryContainer = TextPrimaryLight,
    tertiary = SuccessLight,
    onTertiary = BackgroundLight,
    tertiaryContainer = containerTone(BackgroundLight, SuccessLight),
    onTertiaryContainer = TextPrimaryLight,
    background = BackgroundLight,
    onBackground = TextPrimaryLight,
    surface = BackgroundLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = containerTone(BackgroundLight, TextMutedLight),
    onSurfaceVariant = TextMutedLight,
    surfaceContainerLow = containerTone(BackgroundLight, TextMutedLight, 0.10f),
    outline = TextMutedSecondaryLight,
    error = DangerLight,
    onError = accentOn,
    errorContainer = containerTone(BackgroundLight, DangerLight),
    onErrorContainer = TextPrimaryLight,
)

private fun darkColorSchemeFor(accent: Color, accentOn: Color): ColorScheme = darkColorScheme(
    primary = accent,
    onPrimary = accentOn,
    primaryContainer = containerTone(BackgroundDark, accent),
    onPrimaryContainer = TextPrimaryDark,
    secondary = TextMutedDark,
    onSecondary = BackgroundDark,
    secondaryContainer = containerTone(BackgroundDark, TextMutedDark),
    onSecondaryContainer = TextPrimaryDark,
    tertiary = SuccessDark,
    onTertiary = BackgroundDark,
    tertiaryContainer = containerTone(BackgroundDark, SuccessDark),
    onTertiaryContainer = TextPrimaryDark,
    background = BackgroundDark,
    onBackground = TextPrimaryDark,
    surface = BackgroundDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = containerTone(BackgroundDark, TextMutedDark),
    onSurfaceVariant = TextMutedDark,
    surfaceContainerLow = containerTone(BackgroundDark, TextMutedDark, 0.10f),
    outline = TextMutedSecondaryDark,
    error = DangerDark,
    onError = accentOn,
    errorContainer = containerTone(BackgroundDark, DangerDark),
    onErrorContainer = TextPrimaryDark,
)

// Square-corner geometry app-wide (design §3): every M3 shape slot resolves to a plain rectangle,
// so all `MaterialTheme.shapes.*` consumers (cards, badges, dialogs, sheets) render square. The
// design's rounded exceptions — 48×48dp icon tiles and the 42×42dp gear (12dp radius) and circular
// accent swatches — are drawn with explicit shapes at their call sites, not these slots.
// M3 shape slots require a CornerBasedShape, so square corners are RoundedCornerShape(0.dp)
// (not RectangleShape, which is a plain Shape).
private val SquareShape = RoundedCornerShape(0.dp)

val IOMotionShapes = Shapes(
    extraSmall = SquareShape,
    small = SquareShape,
    medium = SquareShape,
    large = SquareShape,
    extraLarge = SquareShape,
)

@Composable
fun IO_motionTheme(
    themeMode: ThemeMode = ThemeMode.DARK,
    accentTheme: AccentTheme = AccentTheme.ORANGE,
    content: @Composable () -> Unit,
) {
    val darkTheme = themeMode == ThemeMode.DARK
    val accent = accentTheme.toColor()
    val accentOn = accentOnColorFor(accent)

    val colorScheme = remember(darkTheme, accent, accentOn) {
        if (darkTheme) darkColorSchemeFor(accent, accentOn) else lightColorSchemeFor(accent, accentOn)
    }
    val extendedColors = remember(darkTheme, accentOn) {
        (if (darkTheme) DarkExtendedColors else LightExtendedColors).copy(accentOn = accentOn)
    }

    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = IOMotionShapes,
        ) {
            // Guarantees every screen paints over the full window with the resolved theme
            // background, regardless of what the Activity's static XML theme happens to show.
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                content()
            }
        }
    }
}
