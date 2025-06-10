package com.paligot.confily.backend.third.parties.welovedevs.domain

import com.paligot.confily.models.Job

interface JobRepository {
    suspend fun import(eventId: String): List<Job>
}
