@file:OptIn(androidx.compose.ui.text.ExperimentalTextApi::class)

package com.example.io_motion.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.io_motion.core.ui.R

/**
 * Display / heading family (doc/claude_code_redesign_plan_newest.md §3). Poppins has no official
 * variable file, so the weights ship as distinct static TTFs (600/700/800). Requesting a weight the
 * family doesn't carry resolves to the nearest bundled static instance.
 */
val Poppins = FontFamily(
    Font(R.font.poppins_semibold, weight = FontWeight.W600),
    Font(R.font.poppins_bold, weight = FontWeight.W700),
    Font(R.font.poppins_extrabold, weight = FontWeight.W800),
)

/**
 * Body / label family. Manrope ships as a single variable font (wght axis); each weight below is a
 * distinct named instance of the same underlying file — Android instantiates it per
 * [FontVariation.Settings] on API 26+; pre-26 devices fall back to the font's default instance for
 * all weights (a graceful, non-crashing degradation).
 */
val Manrope = FontFamily(
    Font(R.font.manrope_variable, weight = FontWeight.W400, variationSettings = FontVariation.Settings(FontVariation.weight(400))),
    Font(R.font.manrope_variable, weight = FontWeight.W500, variationSettings = FontVariation.Settings(FontVariation.weight(500))),
    Font(R.font.manrope_variable, weight = FontWeight.W600, variationSettings = FontVariation.Settings(FontVariation.weight(600))),
    Font(R.font.manrope_variable, weight = FontWeight.W700, variationSettings = FontVariation.Settings(FontVariation.weight(700))),
    Font(R.font.manrope_variable, weight = FontWeight.W800, variationSettings = FontVariation.Settings(FontVariation.weight(800))),
)

// Material typography — body slot uses Manrope so any stock M3 component matches the design body.
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

/**
 * Named text styles for the app (doc/claude_code_redesign_plan_newest.md §3). Headings, big numbers
 * and the wordmark use Poppins; body, labels and meta use Manrope. Kept as a stable API surface so
 * consuming screens change minimally when the underlying scale/fonts are swapped.
 */
object IOMotionTextStyles {
    val wordmark = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.W800, fontSize = 26.sp)
    val greeting = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.W700, fontSize = 24.sp)
    val subtitle = TextStyle(fontFamily = Manrope, fontWeight = FontWeight.W700, fontSize = 11.sp, letterSpacing = 2.sp)

    // Section labels: 12sp, W800, +1.4 tracking, uppercase, muted, prefixed with an 8×8dp accent
    // square bullet (rendered by the SectionLabel composable).
    val sectionLabel = TextStyle(fontFamily = Manrope, fontWeight = FontWeight.W800, fontSize = 12.sp, letterSpacing = 1.4.sp)

    val exerciseRowName = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.W700, fontSize = 21.sp)

    // Bordered-card / list-row title (workout cards, suggested-meal cards): Poppins 700 16sp.
    val cardTitle = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.W700, fontSize = 16.sp)

    val segmentedLabel = TextStyle(fontFamily = Manrope, fontWeight = FontWeight.W700, fontSize = 13.sp)
    val modelVariantCaption = TextStyle(fontFamily = Manrope, fontWeight = FontWeight.W400, fontSize = 12.sp)

    val ctaLabel = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.W800, fontSize = 16.sp, letterSpacing = 1.sp)

    val screenTitle = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.W800, fontSize = 22.sp)
    val historyTitle = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.W800, fontSize = 24.sp)

    val metaTimestamp = TextStyle(fontFamily = Manrope, fontWeight = FontWeight.W400, fontSize = 12.sp)
    val metaModeVariant = TextStyle(fontFamily = Manrope, fontWeight = FontWeight.W700, fontSize = 12.sp, letterSpacing = 1.sp)

    val scoreCaption = TextStyle(fontFamily = Manrope, fontWeight = FontWeight.W700, fontSize = 11.sp, letterSpacing = 2.sp)
    val scoreNumber = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.W800, fontSize = 56.sp)

    // Big stat / ring numbers (home stat grid, diet calorie ring): Poppins W800 30sp.
    val bigNumber = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.W800, fontSize = 30.sp)

    val statValue = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.W700, fontSize = 24.sp)
    val statLabel = TextStyle(fontFamily = Manrope, fontWeight = FontWeight.W700, fontSize = 10.sp, letterSpacing = 1.2.sp)

    val repTag = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.W700, fontSize = 15.sp)
    val repAngleRange = TextStyle(fontFamily = Manrope, fontWeight = FontWeight.W600, fontSize = 16.sp)
    val repMeta = TextStyle(fontFamily = Manrope, fontWeight = FontWeight.W400, fontSize = 12.sp)
    val repScore = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.W800, fontSize = 20.sp)

    val sessionRowName = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.W700, fontSize = 19.sp)
    val sessionRowMeta = TextStyle(fontFamily = Manrope, fontWeight = FontWeight.W700, fontSize = 11.sp, letterSpacing = 1.sp)
    val sessionStatValue = TextStyle(fontFamily = Poppins, fontWeight = FontWeight.W800, fontSize = 22.sp)
    val sessionStatLabel = TextStyle(fontFamily = Manrope, fontWeight = FontWeight.W700, fontSize = 10.sp, letterSpacing = 1.sp)
}
