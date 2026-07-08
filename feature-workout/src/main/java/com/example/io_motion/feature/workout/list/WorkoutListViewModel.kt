package com.example.io_motion.feature.workout.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.io_motion.data.model.Workout
import com.example.io_motion.data.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class WorkoutListUiState(
    val workouts: List<Workout> = emptyList(),
    val isLoading: Boolean = true,
)

@HiltViewModel
class WorkoutListViewModel @Inject constructor(
    private val repository: WorkoutRepository,
) : ViewModel() {

    val uiState: StateFlow<WorkoutListUiState> = repository.workouts
        .map { WorkoutListUiState(workouts = it, isLoading = false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = WorkoutListUiState(),
        )

    fun delete(id: Long) = repository.delete(id)
}
