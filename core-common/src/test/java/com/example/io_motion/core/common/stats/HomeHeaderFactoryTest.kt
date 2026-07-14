package com.example.io_motion.core.common.stats

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Locale

class HomeHeaderFactoryTest {

    private val utc = ZoneId.of("UTC")

    private fun millisAt(year: Int, month: Int, day: Int, hour: Int): Long =
        LocalDateTime.of(year, month, day, hour, 0).atZone(utc).toInstant().toEpochMilli()

    @Test
    fun `greeting buckets by hour`() {
        assertEquals(Greeting.EVENING, HomeHeaderFactory.greetingForHour(4))
        assertEquals(Greeting.MORNING, HomeHeaderFactory.greetingForHour(5))
        assertEquals(Greeting.MORNING, HomeHeaderFactory.greetingForHour(11))
        assertEquals(Greeting.AFTERNOON, HomeHeaderFactory.greetingForHour(12))
        assertEquals(Greeting.AFTERNOON, HomeHeaderFactory.greetingForHour(17))
        assertEquals(Greeting.EVENING, HomeHeaderFactory.greetingForHour(18))
        assertEquals(Greeting.EVENING, HomeHeaderFactory.greetingForHour(23))
    }

    @Test
    fun `date line is uppercase full weekday and short month`() {
        // 2026-07-08 is a Wednesday.
        val header = HomeHeaderFactory.create(
            nowEpochMillis = millisAt(2026, 7, 8, hour = 9),
            zoneId = utc,
            locale = Locale.US,
        )
        assertEquals(Greeting.MORNING, header.greeting)
        assertEquals("WEDNESDAY, JUL 8", header.dateLine)
    }
}
