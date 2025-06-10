package com.paligot.confily.backend.formats.domain

import com.paligot.confily.models.Format

interface FormatRepository {
    suspend fun list(eventId: String): List<Format>
}
