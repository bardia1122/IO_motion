package com.example.io_motion.core.common.stats

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

/** Time-of-day greeting bucket for the Home hub, mapped to display copy by the UI layer. */
enum class Greeting { MORNING, AFTERNOON, EVENING }

/**
 * Everything the hub header needs, derived purely from the clock so it can be unit-tested without
 * Android. [dateLine] is pre-formatted (e.g. "WEDNESDAY, JUL 8") to keep `java.time` out of the
 * feature module.
 */
data class HomeHeader(
    val greeting: Greeting,
    val dateLine: String,
)

object HomeHeaderFactory {

    // Local-time boundaries for the greeting buckets (24h). Morning [5,12), afternoon [12,18),
    // evening otherwise.
    private const val MORNING_START_HOUR = 5
    private const val AFTERNOON_START_HOUR = 12
    private const val EVENING_START_HOUR = 18

    fun greetingForHour(hour: Int): Greeting = when (hour) {
        in MORNING_START_HOUR until AFTERNOON_START_HOUR -> Greeting.MORNING
        in AFTERNOON_START_HOUR until EVENING_START_HOUR -> Greeting.AFTERNOON
        else -> Greeting.EVENING
    }

    fun create(nowEpochMillis: Long, zoneId: ZoneId, locale: Locale = Locale.getDefault()): HomeHeader {
        val now = LocalDateTime.ofInstant(Instant.ofEpochMilli(nowEpochMillis), zoneId)
        val dayName = now.dayOfWeek.getDisplayName(TextStyle.FULL, locale)
        val monthName = now.month.getDisplayName(TextStyle.SHORT, locale)
        val dateLine = "$dayName, $monthName ${now.dayOfMonth}".uppercase(locale)
        return HomeHeader(greeting = greetingForHour(now.hour), dateLine = dateLine)
    }
}
