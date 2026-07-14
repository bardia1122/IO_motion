package com.example.io_motion.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Design tokens from doc/CLAUDE_CODE_PROMPT_DESIGN.md that don't map onto a standard M3
 * colorScheme role (muted text tiers, hairline dividers, the score-color trio).
 */
data class ExtendedColors(
    val textMuted: Color,
    val textMutedSecondary: Color,
    val hairline: Color,
    val segmentedTrackBorder: Color,
    val subtleFill: Color,
    val accentOn: Color,
    val success: Color,
    val warning: Color,
    val danger: Color,
)

// accentOn is overridden per the current AccentTheme by IO_motionTheme (see Theme.kt); the
// value here is just a sensible default for previews/tests that don't go through that path.
val LightExtendedColors = ExtendedColors(
    textMuted = TextMutedLight,
    textMutedSecondary = TextMutedSecondaryLight,
    hairline = HairlineLight.copy(alpha = HairlineLightAlpha),
    segmentedTrackBorder = SegmentedTrackBorderLight.copy(alpha = SegmentedTrackBorderLightAlpha),
    subtleFill = SubtleFillLight.copy(alpha = SubtleFillLightAlpha),
    accentOn = LightOnAccent,
    success = SuccessLight,
    warning = WarningLight,
    danger = DangerLight,
)

val DarkExtendedColors = ExtendedColors(
    textMuted = TextMutedDark,
    textMutedSecondary = TextMutedSecondaryDark,
    hairline = HairlineDark.copy(alpha = HairlineDarkAlpha),
    segmentedTrackBorder = SegmentedTrackBorderDark.copy(alpha = SegmentedTrackBorderDarkAlpha),
    subtleFill = SubtleFillDark.copy(alpha = SubtleFillDarkAlpha),
    accentOn = LightOnAccent,
    success = SuccessDark,
    warning = WarningDark,
    danger = DangerDark,
)

val LocalExtendedColors = staticCompositionLocalOf { LightExtendedColors }

val MaterialTheme.extendedColors: ExtendedColors
    @Composable get() = LocalExtendedColors.current

/** Status tiers: Good (>=80) -> success; Mid (50–79) -> warning; Poor (<50) -> danger. */
fun ExtendedColors.scoreColor(score: Int): Color = when {
    score >= 80 -> success
    score >= 50 -> warning
    else        -> danger
}
