package org.gdglille.devfest.android.core.models.factory

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import org.gdglille.devfest.models.ScheduleItemV4

fun ScheduleItemV4.Companion.builder(): ScheduleBuilder = ScheduleBuilder()

class ScheduleBuilder {
    private var id: String = ""
    private var date: String = ""
    private var room: String = ""
    private var sessionId: String = ""
    private var order: Int = 0
    private var startTime: String = ""
    private var endTime: String = ""

    fun id(id: String) = apply {
        this.id = id
        this.sessionId = id
    }
    fun room(room: String) = apply { this.room = room }
    fun order(order: Int) = apply { this.order = order }
    fun startTime(startTime: Instant) = apply {
        val start = startTime.formatISO()
        val instant = LocalDateTime.parse(start)
        val format = LocalDateTime.Format {
            year(); char('-'); monthNumber(); char('-'); dayOfMonth()
        }
        this.date = instant.format(format)
        this.startTime = start
    }
    fun endTime(end: Instant) = apply { this.endTime = end.formatISO() }

    fun build(): ScheduleItemV4 = ScheduleItemV4(
        id = id,
        date = date,
        room = room,
        sessionId = id,
        order = order,
        startTime = startTime,
        endTime = endTime
    )
}
