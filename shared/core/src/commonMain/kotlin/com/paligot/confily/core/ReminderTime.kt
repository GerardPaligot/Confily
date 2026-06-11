package com.paligot.confily.core

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

/**
 * Computes the absolute trigger instant (epoch millis) of a session reminder alarm.
 *
 * [startTime] is the session's wall-clock start time at the event location (e.g. "2026-06-11T08:30")
 * and carries no timezone. It is interpreted in [timeZone] before being turned into an absolute
 * instant: pass the device timezone so the alarm fires at the time displayed in the app rather than
 * being offset by the difference from UTC. The reminder is scheduled [reminderInMinutes] earlier.
 */
internal fun reminderTriggerAtMillis(
    startTime: String,
    timeZone: TimeZone,
    reminderInMinutes: Int
): Long = startTime
    .toLocalDateTime()
    .toInstant(timeZone)
    .minus(reminderInMinutes, DateTimeUnit.MINUTE)
    .toEpochMilliseconds()
