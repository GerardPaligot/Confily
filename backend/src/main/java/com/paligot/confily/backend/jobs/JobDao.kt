package com.paligot.confily.backend.jobs

import com.google.cloud.firestore.Firestore
import com.paligot.confily.backend.internals.helpers.database.batchDelete
import com.paligot.confily.backend.internals.helpers.database.getDocuments
import com.paligot.confily.backend.internals.helpers.database.insert
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

private const val CollectionName = "jobs"

class JobDao(
    private val projectName: String,
    private val firestore: Firestore
) {
    fun getAll(eventId: String): List<JobDb> = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName)
        .getDocuments<JobDb>()

    suspend fun resetJobs(eventId: String, jobs: List<JobDb>) = coroutineScope {
        val docRefs = firestore
            .collection(projectName)
            .document(eventId)
            .collection(CollectionName)
            .listDocuments()
            .toList()
        firestore.batchDelete(docRefs)
        val asyncItems = jobs.map { job ->
            async {
                firestore
                    .collection(projectName)
                    .document(eventId)
                    .collection(CollectionName)
                    .insert(job.id, job)
            }
        }
        asyncItems.awaitAll()
        Unit
    }
}
