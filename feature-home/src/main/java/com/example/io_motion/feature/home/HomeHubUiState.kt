package com.example.io_motion.feature.home

import com.example.io_motion.core.common.stats.Greeting
import com.example.io_motion.core.common.stats.HomeStats

/**
 * State for the Home hub. Defaults represent the pre-data frame (empty history, resolved on the
 * first [com.example.io_motion.data.repository.SessionRepository.sessions] emission).
 */
data class HomeHubUiState(
    val greeting: Greeting = Greeting.MORNING,
    val dateLine: String = "",
    val stats: HomeStats = HomeStats(lastQuality = null, sessionsThisWeek = 0, dayStreak = 0),
)
