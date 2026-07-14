package com.example.io_motion.feature.workout

import com.example.io_motion.core.common.models.displayName
import com.example.io_motion.data.model.WorkoutItem

/**
 * One-line summary of a workout's routine, e.g. "Squat ×3 · Plank ×3" (each item's exercise name
 * and set count). Pure so it can be unit-tested on the JVM.
 */
fun workoutSummary(items: List<WorkoutItem>): String =
    items.joinToString(separator = " · ") { "${it.exerciseType.displayName()} ×${it.sets}" }
