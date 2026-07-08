package com.example.io_motion.core.ui.overlay

import androidx.compose.ui.graphics.Color
import com.example.io_motion.core.ui.theme.StatusGood

/**
 * Colors for the live camera skeleton overlay. Exercise-relevant ("primary") joints are drawn in
 * the shared Good status color so the overlay reads from the same palette as the report screens;
 * all other joints are white (readable over any camera background).
 */
internal object SkeletonColors {
    val defaultLine = Color.White.copy(alpha = 0.65f)
    val primaryLine = StatusGood.copy(alpha = 0.9f)
    val defaultJoint = Color.White
    val primaryJoint = StatusGood
}
