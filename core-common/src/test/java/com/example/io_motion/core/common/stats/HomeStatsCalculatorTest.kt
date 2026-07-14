package com.example.io_motion.core.common.stats

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset

class HomeStatsCalculatorTest {

    private val utc = ZoneId.of("UTC")

    /** Epoch millis for a wall-clock date/time in [zone]. */
    private fun millisAt(
        year: Int, month: Int, day: Int, hour: Int = 12, minute: Int = 0,
        zone: ZoneId = utc,
    ): Long = LocalDateTime.of(year, month, day, hour, minute)
        .atZone(zone).toInstant().toEpochMilli()

    private fun session(millis: Long, quality: Int = 70) = StatSession(millis, quality)

    // ── Empty history ───────────────────────────────────────────────────────────

    @Test
    fun `empty history yields null quality and zero counts`() {
        val stats = HomeStatsCalculator.compute(
            sessions = emptyList(),
            nowEpochMillis = millisAt(2026, 7, 8),
            zoneId = utc,
        )
        assertNull(stats.lastQuality)
        assertEquals(0, stats.sessionsThisWeek)
        assertEquals(0, stats.dayStreak)
    }

    // ── Last quality ────────────────────────────────────────────────────────────

    @Test
    fun `last quality is the most recent session regardless of list order`() {
        val stats = HomeStatsCalculator.compute(
            sessions = listOf(
                session(millisAt(2026, 7, 1), quality = 40),
                session(millisAt(2026, 7, 8), quality = 91), // newest
                session(millisAt(2026, 7, 5), quality = 60),
            ),
            nowEpochMillis = millisAt(2026, 7, 8, hour = 20),
            zoneId = utc,
        )
        assertEquals(91, stats.lastQuality)
    }

    // ── Sessions this week (Monday start) ───────────────────────────────────────

    @Test
    fun `sessions this week counts Monday-anchored week only`() {
        // Wed 2026-07-08. Week runs Mon 2026-07-06 .. Sun 2026-07-12.
        val now = millisAt(2026, 7, 8, hour = 9)
        val stats = HomeStatsCalculator.compute(
            sessions = listOf(
                session(millisAt(2026, 7, 6)),  // Monday — in week
                session(millisAt(2026, 7, 8)),  // Wednesday (today) — in week
                session(millisAt(2026, 7, 5)),  // Sunday prior — previous week, excluded
                session(millisAt(2026, 6, 30)), // last month — excluded
            ),
            nowEpochMillis = now,
            zoneId = utc,
        )
        assertEquals(2, stats.sessionsThisWeek)
    }

    @Test
    fun `monday just after midnight excludes the previous sunday`() {
        // Monday 2026-07-06 00:05. Sunday 2026-07-05 23:55 is the previous week.
        val stats = HomeStatsCalculator.compute(
            sessions = listOf(
                session(millisAt(2026, 7, 6, hour = 0, minute = 5)),  // this week
                session(millisAt(2026, 7, 5, hour = 23, minute = 55)), // last week
            ),
            nowEpochMillis = millisAt(2026, 7, 6, hour = 0, minute = 5),
            zoneId = utc,
        )
        assertEquals(1, stats.sessionsThisWeek)
    }

    @Test
    fun `week membership respects timezone`() {
        // A session at 2026-07-06 01:00 UTC is still 2026-07-05 (Sunday) in UTC-05:00, so in a
        // Monday-start week anchored on 2026-07-06 it should be excluded when computed in that zone.
        val zone = ZoneOffset.ofHours(-5)
        val stats = HomeStatsCalculator.compute(
            sessions = listOf(session(millisAt(2026, 7, 6, hour = 1, zone = utc))),
            nowEpochMillis = LocalDate.of(2026, 7, 8).atTime(LocalTime.NOON).atZone(zone).toInstant().toEpochMilli(),
            zoneId = zone,
        )
        assertEquals(0, stats.sessionsThisWeek)
    }

    // ── Day streak ──────────────────────────────────────────────────────────────

    @Test
    fun `streak counts consecutive days ending today`() {
        val stats = HomeStatsCalculator.compute(
            sessions = listOf(
                session(millisAt(2026, 7, 8)),
                session(millisAt(2026, 7, 7)),
                session(millisAt(2026, 7, 6)),
            ),
            nowEpochMillis = millisAt(2026, 7, 8, hour = 20),
            zoneId = utc,
        )
        assertEquals(3, stats.dayStreak)
    }

    @Test
    fun `streak allows ending yesterday when nothing logged today`() {
        // No session today (2026-07-08); streak runs through yesterday and back.
        val stats = HomeStatsCalculator.compute(
            sessions = listOf(
                session(millisAt(2026, 7, 7)),
                session(millisAt(2026, 7, 6)),
            ),
            nowEpochMillis = millisAt(2026, 7, 8, hour = 8),
            zoneId = utc,
        )
        assertEquals(2, stats.dayStreak)
    }

    @Test
    fun `streak breaks on a gap`() {
        // Today + a gap two days ago -> only today counts.
        val stats = HomeStatsCalculator.compute(
            sessions = listOf(
                session(millisAt(2026, 7, 8)),
                session(millisAt(2026, 7, 6)), // gap on the 7th
            ),
            nowEpochMillis = millisAt(2026, 7, 8, hour = 20),
            zoneId = utc,
        )
        assertEquals(1, stats.dayStreak)
    }

    @Test
    fun `streak is zero when the most recent session is older than yesterday`() {
        val stats = HomeStatsCalculator.compute(
            sessions = listOf(session(millisAt(2026, 7, 5))),
            nowEpochMillis = millisAt(2026, 7, 8),
            zoneId = utc,
        )
        assertEquals(0, stats.dayStreak)
    }

    @Test
    fun `multiple sessions on the same day count once toward the streak`() {
        val stats = HomeStatsCalculator.compute(
            sessions = listOf(
                session(millisAt(2026, 7, 8, hour = 7)),
                session(millisAt(2026, 7, 8, hour = 19)),
                session(millisAt(2026, 7, 7, hour = 12)),
            ),
            nowEpochMillis = millisAt(2026, 7, 8, hour = 21),
            zoneId = utc,
        )
        assertEquals(2, stats.dayStreak)
    }
}
