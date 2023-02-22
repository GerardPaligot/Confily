package org.gdglille.devfest.backend.jobs

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.gdglille.devfest.backend.internals.helpers.database.Database
import org.gdglille.devfest.backend.internals.helpers.database.get
import org.gdglille.devfest.backend.internals.helpers.database.getAll

private const val CollectionName = "jobs"

class JobDao(private val database: Database) {
    suspend fun getAll(eventId: String): List<JobDb> = database
        .getAll(eventId = eventId, collectionName = CollectionName)

    suspend fun createOrUpdate(eventId: String, job: JobDb): String = coroutineScope {
        val existing = database.get<JobDb>(
            eventId = eventId,
            collectionName = CollectionName,
            id = job.id
        )
        if (existing == null) database.insert(
            eventId = eventId, collectionName = CollectionName,
            id = job.id,
            item = job
        )
        else database.update(
            eventId = eventId,
            collectionName = CollectionName,
            id = job.id,
            item = job
        )
        return@coroutineScope job.id
    }

    suspend fun createOrUpdateAll(eventId: String, jobs: List<JobDb>) = coroutineScope {
        val asyncItems = jobs.map {
            async {
                createOrUpdate(
                    eventId = eventId,
                    job = it
                )
            }
        }
        asyncItems.awaitAll()
        Unit
    }
}
