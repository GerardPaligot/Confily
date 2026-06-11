package com.paligot.confily.core

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ReminderTimeTest {
    @Test
    fun reminder_trigger_uses_event_local_wall_clock_not_utc() {
        // "2026-06-11T08:30" is a wall-clock time at the event (Lille / Europe/Paris, CEST = UTC+2
        // in June) and carries no timezone. A 10-minute reminder must fire at 08:20 local = 06:20 UTC.
        val triggerAtMillis = reminderTriggerAtMillis(
            startTime = "2026-06-11T08:30",
            timeZone = TimeZone.of("Europe/Paris"),
            reminderInMinutes = 10
        )

        assertEquals(
            Instant.parse("2026-06-11T06:20:00Z").toEpochMilliseconds(),
            triggerAtMillis
        )
        // Regression guard: the previous code interpreted the wall-clock time as UTC, which would
        // have fired the alarm at 08:20 UTC (10:20 local) — two hours late.
        assertNotEquals(
            Instant.parse("2026-06-11T08:20:00Z").toEpochMilliseconds(),
            triggerAtMillis
        )
    }
}
