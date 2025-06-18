package com.paligot.confily.backend.export.domain

interface ExportRepository<T> {
    suspend fun get(eventId: String): T
}
