package com.example.io_motion.feature.workout

import com.example.io_motion.core.common.models.ExerciseType
import com.example.io_motion.data.model.WorkoutItem
import org.junit.Assert.assertEquals
import org.junit.Test

class WorkoutSummaryTest {

    private fun item(type: ExerciseType, sets: Int, position: Int) =
        WorkoutItem(id = 0, exerciseType = type, sets = sets, reps = 10, position = position)

    @Test
    fun `formats each item as name and set count joined by middot`() {
        val summary = workoutSummary(
            listOf(
                item(ExerciseType.SQUAT, sets = 3, position = 0),
                item(ExerciseType.PLANK, sets = 3, position = 1),
            )
        )
        assertEquals("Squat ×3 · Plank ×3", summary)
    }

    @Test
    fun `keeps duplicate exercises as separate segments`() {
        val summary = workoutSummary(
            listOf(
                item(ExerciseType.SQUAT, sets = 3, position = 0),
                item(ExerciseType.SQUAT, sets = 2, position = 1),
            )
        )
        assertEquals("Squat ×3 · Squat ×2", summary)
    }

    @Test
    fun `empty routine yields empty string`() {
        assertEquals("", workoutSummary(emptyList()))
    }
}
