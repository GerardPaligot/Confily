package com.paligot.confily.backend.schedules.domain

import com.paligot.confily.models.ScheduleItem

interface ScheduleRepository {
    suspend fun get(eventId: String, scheduleId: String): ScheduleItem
}
