package com.example.io_motion.feature.workout.builder

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.io_motion.core.common.models.ExerciseType
import com.example.io_motion.data.model.Workout
import com.example.io_motion.data.model.WorkoutItem
import com.example.io_motion.data.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** One draft routine row. [localId] is a UI-only key (duplicates of an exercise are distinct rows). */
data class DraftItem(
    val localId: Long,
    val exerciseType: ExerciseType,
    val sets: Int,
    /** Reps per set; for [ExerciseType.PLANK] this value is seconds of hold. */
    val reps: Int,
)

data class WorkoutBuilderUiState(
    val isEditing: Boolean = false,
    val name: String = "",
    val items: List<DraftItem> = emptyList(),
)

@HiltViewModel
class WorkoutBuilderViewModel @Inject constructor(
    private val repository: WorkoutRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    // Optional nav arg: -1L (default) means "new workout".
    private val editingId: Long? = savedStateHandle.get<Long>(ARG_WORKOUT_ID)?.takeIf { it > 0 }
    private var loadedCreatedAt: Long? = null
    private var loadedSortOrder: Int = 0
    private var nextLocalId = 0L

    private val _uiState = MutableStateFlow(WorkoutBuilderUiState(isEditing = editingId != null))
    val uiState = _uiState.asStateFlow()

    // One-shot "saved, navigate back" signal.
    private val _saved = Channel<Unit>(Channel.BUFFERED)
    val saved = _saved.receiveAsFlow()

    init {
        editingId?.let { load(it) }
    }

    private fun load(id: Long) {
        viewModelScope.launch {
            val workout = repository.getById(id) ?: return@launch
            loadedCreatedAt = workout.createdAt
            loadedSortOrder = workout.sortOrder
            _uiState.update { state ->
                state.copy(
                    name = workout.name,
                    items = workout.items
                        .sortedBy { it.position }
                        .map { DraftItem(nextLocalId++, it.exerciseType, it.sets, it.reps) },
                )
            }
        }
    }

    fun setName(name: String) = _uiState.update { it.copy(name = name) }

    fun addExercise(type: ExerciseType) = _uiState.update {
        it.copy(items = it.items + DraftItem(nextLocalId++, type, DEFAULT_SETS, DEFAULT_REPS))
    }

    fun removeItem(localId: Long) = _uiState.update {
        it.copy(items = it.items.filterNot { item -> item.localId == localId })
    }

    fun stepSets(localId: Long, up: Boolean) = updateItem(localId) { item ->
        item.copy(sets = (item.sets + if (up) 1 else -1).coerceAtLeast(SETS_MIN))
    }

    fun stepReps(localId: Long, up: Boolean) = updateItem(localId) { item ->
        val isPlank = item.exerciseType == ExerciseType.PLANK
        val step = if (isPlank) PLANK_SEC_STEP else REPS_STEP
        val min = if (isPlank) PLANK_SEC_MIN else REPS_MIN
        item.copy(reps = (item.reps + if (up) step else -step).coerceAtLeast(min))
    }

    private inline fun updateItem(localId: Long, crossinline transform: (DraftItem) -> DraftItem) =
        _uiState.update { state ->
            state.copy(items = state.items.map { if (it.localId == localId) transform(it) else it })
        }

    fun save() {
        val state = _uiState.value
        val workout = Workout(
            id = editingId ?: 0L,
            name = state.name.trim().ifBlank { UNTITLED_NAME },
            createdAt = loadedCreatedAt ?: System.currentTimeMillis(),
            sortOrder = loadedSortOrder,
            items = state.items.mapIndexed { index, item ->
                WorkoutItem(
                    id = 0,
                    exerciseType = item.exerciseType,
                    sets = item.sets,
                    reps = item.reps,
                    position = index,
                )
            },
        )
        repository.upsert(workout)
        _saved.trySend(Unit)
    }

    companion object {
        const val ARG_WORKOUT_ID = "workoutId"
        const val UNTITLED_NAME = "Untitled Workout"
        const val DEFAULT_SETS = 3
        const val DEFAULT_REPS = 10
        const val SETS_MIN = 1
        const val REPS_MIN = 1
        const val REPS_STEP = 1
        const val PLANK_SEC_MIN = 10
        const val PLANK_SEC_STEP = 10
    }
}
