package com.paligot.confily.backend.jobs

import com.paligot.confily.backend.internals.helpers.database.Database
import com.paligot.confily.backend.internals.helpers.database.getAll
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

private const val CollectionName = "jobs"

class JobDao(private val database: Database) {
    suspend fun getAll(eventId: String): List<JobDb> = database
        .getAll(eventId = eventId, collectionName = CollectionName)

    suspend fun resetJobs(eventId: String, jobs: List<JobDb>) = coroutineScope {
        database.delete(eventId, CollectionName)
        val asyncItems = jobs.map { job ->
            async {
                database.insert(
                    eventId = eventId,
                    collectionName = CollectionName,
                    id = job.id,
                    item = job
                )
            }
        }
        asyncItems.awaitAll()
        Unit
    }
}
