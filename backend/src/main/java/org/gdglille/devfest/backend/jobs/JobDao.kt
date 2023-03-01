package org.gdglille.devfest.backend.jobs

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.gdglille.devfest.backend.internals.helpers.database.Database
import org.gdglille.devfest.backend.internals.helpers.database.getAll

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
