package com.example.io_motion.core.common.stats

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

/**
 * Minimal projection of a persisted session needed to compute the Home hub's "THIS WEEK" stats.
 *
 * Deliberately not `SessionRecord` (which lives in `:data`): keeping the input primitive lets this
 * calculator stay in `:core-common`, which is a pure-JVM leaf module with no Android or `:data`
 * dependency. The hub ViewModel maps each `SessionRecord` to one of these.
 *
 * @param recordedAt Session start, epoch milliseconds.
 * @param qualityScore Session quality score, 0–100.
 */
data class StatSession(
    val recordedAt: Long,
    val qualityScore: Int,
)

/**
 * The three hub statistics.
 *
 * @param lastQuality Quality of the most recent session (any time), or `null` when there is no
 *   history yet (rendered as "—").
 * @param sessionsThisWeek Count of sessions in the current calendar week (Monday start, device zone).
 * @param dayStreak Consecutive calendar days — ending today, or yesterday if none yet today — that
 *   each have at least one session.
 */
data class HomeStats(
    val lastQuality: Int?,
    val sessionsThisWeek: Int,
    val dayStreak: Int,
)

/**
 * Pure, timezone-aware calculator for the Home hub stats. All time inputs are primitives so this
 * class can live in `:core-common` and be exhaustively unit-tested with crafted timestamps.
 */
object HomeStatsCalculator {

    fun compute(sessions: List<StatSession>, nowEpochMillis: Long, zoneId: ZoneId): HomeStats {
        if (sessions.isEmpty()) {
            return HomeStats(lastQuality = null, sessionsThisWeek = 0, dayStreak = 0)
        }

        // Last Quality — most recent session by wall-clock start.
        val lastQuality = sessions.maxByOrNull { it.recordedAt }!!.qualityScore

        val today = LocalDate.ofInstant(Instant.ofEpochMilli(nowEpochMillis), zoneId)
        val thisWeekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

        fun StatSession.localDate(): LocalDate =
            LocalDate.ofInstant(Instant.ofEpochMilli(recordedAt), zoneId)

        // Sessions this week — same Monday-anchored week as today (handles any day in the week,
        // including a session recorded later "today").
        val sessionsThisWeek = sessions.count { session ->
            session.localDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)) == thisWeekStart
        }

        // Day streak — walk back one day at a time over the set of dates that have >=1 session.
        val activeDates: Set<LocalDate> = sessions.mapTo(HashSet()) { it.localDate() }
        var cursor: LocalDate? = when {
            today in activeDates -> today
            today.minusDays(1) in activeDates -> today.minusDays(1)
            else -> null
        }
        var dayStreak = 0
        while (cursor != null && cursor in activeDates) {
            dayStreak++
            cursor = cursor.minusDays(1)
        }

        return HomeStats(lastQuality = lastQuality, sessionsThisWeek = sessionsThisWeek, dayStreak = dayStreak)
    }
}
