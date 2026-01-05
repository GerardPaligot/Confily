package com.paligot.confily.backend.planning.domain

import com.paligot.confily.models.Agenda
import com.paligot.confily.models.AgendaV3
import com.paligot.confily.models.AgendaV4
import com.paligot.confily.models.ScheduleItem

interface PlanningRepository {
    suspend fun getCsv(eventId: String): String
    suspend fun agenda(eventId: String): Agenda
    suspend fun agendaMultiDays(eventId: String): Map<String, Map<String, List<ScheduleItem>>>
    suspend fun planning(eventId: String): AgendaV3
    suspend fun planningBySchedules(eventId: String): AgendaV4
}
