package com.example.io_motion.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.io_motion.core.common.stats.HomeHeaderFactory
import com.example.io_motion.core.common.stats.HomeStatsCalculator
import com.example.io_motion.core.common.stats.StatSession
import com.example.io_motion.data.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.ZoneId
import javax.inject.Inject

/**
 * Derives the hub header (greeting + date) and the "THIS WEEK" stats from the persisted session
 * history. All non-trivial logic lives in [HomeStatsCalculator] / [HomeHeaderFactory] (pure,
 * unit-tested in :core-common); this ViewModel only supplies the current clock and maps records.
 */
@HiltViewModel
class HomeHubViewModel @Inject constructor(
    sessionRepository: SessionRepository,
) : ViewModel() {

    val uiState: StateFlow<HomeHubUiState> = sessionRepository.sessions
        .map { records ->
            val now = System.currentTimeMillis()
            val zone = ZoneId.systemDefault()
            val header = HomeHeaderFactory.create(now, zone)
            val stats = HomeStatsCalculator.compute(
                sessions = records.map { StatSession(it.recordedAt, it.qualityScore) },
                nowEpochMillis = now,
                zoneId = zone,
            )
            HomeHubUiState(greeting = header.greeting, dateLine = header.dateLine, stats = stats)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeHubUiState(),
        )
}
