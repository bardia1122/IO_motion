package com.example.io_motion.core.ui.theme

import androidx.compose.ui.graphics.Color
import com.example.io_motion.core.common.models.AccentTheme

// ── Design tokens — dark theme (doc/claude_code_redesign_plan_newest.md §3) ─────
val BackgroundDark = Color(0xFF1C1509)
val TextPrimaryDark = Color(0xFFF5EEDF)
val TextMutedDark = Color(0xFF93886F)
// The new design language defines a single muted tier; the secondary tier is kept for existing
// call sites and mapped to the same value so older screens keep compiling and reading cleanly.
val TextMutedSecondaryDark = Color(0xFF93886F)
val HairlineDark = Color(0xFFF5EEDF)
const val HairlineDarkAlpha = 0.16f
val SegmentedTrackBorderDark = Color(0xFFF5EEDF)
const val SegmentedTrackBorderDarkAlpha = 0.16f
// Subtle fill behind 48×48dp icon tiles.
val SubtleFillDark = Color(0xFFF5EEDF)
const val SubtleFillDarkAlpha = 0.06f

// ── Design tokens — light theme ─────────────────────────────────────────────────
val BackgroundLight = Color(0xFFF5EEDF)
val TextPrimaryLight = Color(0xFF211A0F)
val TextMutedLight = Color(0xFF8C826F)
val TextMutedSecondaryLight = Color(0xFF8C826F)
val HairlineLight = Color(0xFF211A0F)
const val HairlineLightAlpha = 0.15f
val SegmentedTrackBorderLight = Color(0xFF211A0F)
const val SegmentedTrackBorderLightAlpha = 0.15f
val SubtleFillLight = Color(0xFF211A0F)
const val SubtleFillLightAlpha = 0.04f

// ── Accent — user-selectable, same across both themes ───────────────────────────
val AccentBlue = Color(0xFF3C6FF0)
val AccentOrange = Color(0xFFFF6A2B)
val AccentLime = Color(0xFFC6F332)

// "Accent-on" contrast rule: luminance = (0.299r + 0.587g + 0.114b); >0.62 -> dark ink, else light.
// Verified against the accents above: Blue≈0.43, Orange≈0.56 -> light ink; Lime≈0.81 -> dark ink,
// matching the handoff's stated #FFF7EC (blue/orange) / #1C1509 (lime) text-on-accent values.
val LightOnAccent = Color(0xFFFFF7EC)
val DarkInkOnAccent = Color(0xFF1C1509)

fun AccentTheme.toColor(): Color = when (this) {
    AccentTheme.BLUE   -> AccentBlue
    AccentTheme.ORANGE -> AccentOrange
    AccentTheme.LIME   -> AccentLime
}

/** Accent-on contrast rule: luminance = (0.299r + 0.587g + 0.114b); >0.62 -> dark ink, else light. */
fun accentOnColorFor(accent: Color): Color {
    val luminance = (0.299f * accent.red * 255f + 0.587f * accent.green * 255f + 0.114f * accent.blue * 255f) / 255f
    return if (luminance > 0.62f) DarkInkOnAccent else LightOnAccent
}

// ── Status colors — session quality (same in both themes) ───────────────────────
// Good (>=80), Mid (50–79), Poor (<50). Used by ring, per-rep scores, history quality numbers,
// and (re-derived) the skeleton overlay.
val StatusGood = Color(0xFF2E9E5B)
val StatusMid = Color(0xFFB8860B)
val StatusPoor = Color(0xFFC1442D)

// Retained aliases so ExtendedColors/consumers that reference per-theme names keep compiling.
val SuccessDark = StatusGood
val WarningDark = StatusMid
val DangerDark = StatusPoor
val SuccessLight = StatusGood
val WarningLight = StatusMid
val DangerLight = StatusPoor
