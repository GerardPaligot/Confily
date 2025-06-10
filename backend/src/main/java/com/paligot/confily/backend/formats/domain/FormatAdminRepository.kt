package com.paligot.confily.backend.formats.domain

import com.paligot.confily.models.inputs.FormatInput

interface FormatAdminRepository {
    suspend fun create(eventId: String, format: FormatInput): String
    suspend fun update(eventId: String, formatId: String, input: FormatInput): String
}
