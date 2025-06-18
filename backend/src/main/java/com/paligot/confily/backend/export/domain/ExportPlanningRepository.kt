package com.paligot.confily.backend.export.domain

import com.paligot.confily.models.AgendaV4

interface ExportPlanningRepository : ExportRepository<AgendaV4> {
    suspend fun getCsv(eventId: String): String
}
