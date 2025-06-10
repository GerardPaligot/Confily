package com.paligot.confily.backend.schedules.domain

import com.paligot.confily.models.inputs.ScheduleInput

interface ScheduleAdminRepository {
    suspend fun create(eventId: String, scheduleInput: ScheduleInput): String
    suspend fun delete(eventId: String, scheduleId: String)
}
