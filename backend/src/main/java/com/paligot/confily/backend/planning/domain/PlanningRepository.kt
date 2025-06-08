package com.paligot.confily.backend.planning.domain

import com.paligot.confily.backend.internals.infrastructure.firestore.EventEntity
import com.paligot.confily.models.Agenda
import com.paligot.confily.models.AgendaV3
import com.paligot.confily.models.AgendaV4
import com.paligot.confily.models.PlanningItem
import com.paligot.confily.models.ScheduleItem

interface PlanningRepository {
    suspend fun agenda(eventDb: EventEntity): Agenda
    suspend fun agendaMultiDays(eventDb: EventEntity): Map<String, Map<String, List<ScheduleItem>>>
    suspend fun agendaMultiDaysAndEventSessions(eventDb: EventEntity): Map<String, Map<String, List<PlanningItem>>>
    suspend fun planning(eventDb: EventEntity): AgendaV3
    suspend fun planningBySchedules(eventDb: EventEntity): AgendaV4
}
