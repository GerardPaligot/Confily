package com.paligot.confily.core.schedules.entities

import com.paligot.confily.resources.EnStrings
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private class FixedClock(private val instant: Instant) : Clock {
    override fun now(): Instant = instant
}

private val parisTimeZone = TimeZone.of("Europe/Paris")

private fun clockAtParisLocalTime(localTime: LocalDateTime): Clock =
    FixedClock(localTime.toInstant(parisTimeZone))

private fun session(
    startTime: LocalDateTime,
    endTime: LocalDateTime,
    feedback: FeedbackConfig? = FeedbackConfig(projectId = "p", sessionId = "s", url = "https://example.com")
) = Session(
    id = "1",
    title = "A talk",
    abstract = "Abstract",
    category = Category(id = "cat", name = "Category", color = "#ffffff", icon = null),
    format = Format(id = "fmt", name = "Talk", time = 30),
    tags = emptyList(),
    room = "Hall exposant",
    level = null,
    language = null,
    startTime = startTime,
    endTime = endTime,
    speakers = emptyList(),
    feedback = feedback
)

class SessionTest {
    @Test
    fun feedbackOpensRightAfterLocalStartTimeInEventTimeZone() {
        // Session starts at 08:30 Europe/Paris (CEST = UTC+2, i.e. 06:30 UTC).
        val start = LocalDateTime(2026, 6, 11, 8, 30)
        val end = LocalDateTime(2026, 6, 11, 9, 0)
        // "Now" is one minute after the local start: 08:31 Paris = 06:31 UTC.
        val now = clockAtParisLocalTime(LocalDateTime(2026, 6, 11, 8, 31))

        val ui = session(start, end).mapToSessionUi(
            strings = EnStrings,
            clock = now,
            timeZone = parisTimeZone
        )

        assertTrue(
            ui.canGiveFeedback,
            "Feedback must open as soon as the session has started in the event time zone"
        )
    }

    @Test
    fun feedbackStaysClosedBeforeLocalStartTime() {
        val start = LocalDateTime(2026, 6, 11, 8, 30)
        val end = LocalDateTime(2026, 6, 11, 9, 0)
        // "Now" is one minute before the local start: 08:29 Paris.
        val now = clockAtParisLocalTime(LocalDateTime(2026, 6, 11, 8, 29))

        val ui = session(start, end).mapToSessionUi(
            strings = EnStrings,
            clock = now,
            timeZone = parisTimeZone
        )

        assertFalse(
            ui.canGiveFeedback,
            "Feedback must stay closed until the session has started"
        )
    }

    @Test
    fun feedbackStaysClosedWhenSessionHasNoFeedbackConfig() {
        val start = LocalDateTime(2026, 6, 11, 8, 30)
        val end = LocalDateTime(2026, 6, 11, 9, 0)
        val now = clockAtParisLocalTime(LocalDateTime(2026, 6, 11, 8, 31))

        val ui = session(start, end, feedback = null).mapToSessionUi(
            strings = EnStrings,
            clock = now,
            timeZone = parisTimeZone
        )

        assertFalse(
            ui.canGiveFeedback,
            "Feedback cannot be given when the session has no feedback configuration"
        )
    }
}
