package com.paligot.confily.backend.partners.domain

import com.paligot.confily.models.Job

interface JobRepository {
    suspend fun import(eventId: String): List<Job>
}
