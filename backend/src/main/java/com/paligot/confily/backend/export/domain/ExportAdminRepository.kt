package com.paligot.confily.backend.export.domain

interface ExportAdminRepository<T> {
    suspend fun export(eventId: String): T
}
