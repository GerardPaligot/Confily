package com.paligot.confily.backend.partners.application

import com.paligot.confily.backend.partners.domain.JobRepository
import com.paligot.confily.models.Job
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

class JobRepositoryExposed(
    private val database: Database
) : JobRepository {
    override suspend fun import(eventId: String): List<Job> = transaction(db = database) {
        TODO("Not implemented yet")
    }
}
