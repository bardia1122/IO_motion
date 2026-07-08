package com.example.io_motion.feature.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.io_motion.core.ui.theme.extendedColors

/** 48×48dp icon tile: 12dp radius, subtle-fill background (design §3). Hosts a vector motif. */
private val TileSize = 48.dp
private val TileRadius = 12.dp

@Composable
private fun IconTile(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .size(TileSize)
            .clip(RoundedCornerShape(TileRadius))
            .background(MaterialTheme.extendedColors.subtleFill),
        contentAlignment = Alignment.Center,
        content = { content() },
    )
}

/** Motion Assessment — a bullseye: 24dp accent circle outline with a centered 8dp accent dot. */
@Composable
fun MotionAssessmentIcon() {
    val accent = MaterialTheme.colorScheme.primary
    IconTile {
        Canvas(modifier = Modifier.size(TileSize)) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val ringDiameter = 24.dp.toPx()
            val ringStroke = 2.dp.toPx()
            drawCircle(
                color = accent,
                radius = (ringDiameter - ringStroke) / 2f,
                center = center,
                style = Stroke(width = ringStroke),
            )
            drawCircle(color = accent, radius = 8.dp.toPx() / 2f, center = center)
        }
    }
}

/** Create Your Workout — 2×2 grid; top-left & bottom-right full opacity, the other two at 55%. */
@Composable
fun CreateWorkoutIcon() {
    val accent = MaterialTheme.colorScheme.primary
    IconTile {
        Canvas(modifier = Modifier.size(TileSize)) {
            val cell = 9.dp.toPx()
            val gap = 3.dp.toPx()
            val block = cell * 2 + gap
            val originX = (size.width - block) / 2f
            val originY = (size.height - block) / 2f
            fun square(col: Int, row: Int, color: Color) = drawRect(
                color = color,
                topLeft = Offset(originX + col * (cell + gap), originY + row * (cell + gap)),
                size = Size(cell, cell),
            )
            val faded = accent.copy(alpha = 0.55f)
            square(0, 0, accent) // top-left
            square(1, 0, faded)  // top-right
            square(0, 1, faded)  // bottom-left
            square(1, 1, accent) // bottom-right
        }
    }
}

/** Diet Planning — three bottom-aligned accent bars of heights 11 / 19 / 14dp. */
@Composable
fun DietPlanningIcon() {
    val accent = MaterialTheme.colorScheme.primary
    IconTile {
        Canvas(modifier = Modifier.size(TileSize)) {
            val barWidth = 5.dp.toPx()
            val gap = 4.dp.toPx()
            val heights = listOf(11.dp, 19.dp, 14.dp).map { it.toPx() }
            val totalWidth = barWidth * heights.size + gap * (heights.size - 1)
            val originX = (size.width - totalWidth) / 2f
            val baseline = (size.height + heights.max()) / 2f
            heights.forEachIndexed { index, h ->
                val left = originX + index * (barWidth + gap)
                drawRect(
                    color = accent,
                    topLeft = Offset(left, baseline - h),
                    size = Size(barWidth, h),
                )
            }
        }
    }
}
