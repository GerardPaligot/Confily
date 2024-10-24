package com.paligot.confily.backend.schedules

import com.google.cloud.firestore.Firestore
import com.paligot.confily.backend.internals.helpers.database.batchDelete
import com.paligot.confily.backend.internals.helpers.database.delete
import com.paligot.confily.backend.internals.helpers.database.diff
import com.paligot.confily.backend.internals.helpers.database.getDocument
import com.paligot.confily.backend.internals.helpers.database.getDocuments
import com.paligot.confily.backend.internals.helpers.database.upsert

private const val CollectionName = "schedule-items"

class ScheduleItemDao(
    private val projectName: String,
    private val firestore: Firestore
) {
    fun get(eventId: String, id: String): ScheduleDb? = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName)
        .getDocument(id)

    fun getAll(eventId: String): List<ScheduleDb> = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName)
        .getDocuments<ScheduleDb>()

    fun createOrUpdate(eventId: String, item: ScheduleDb) {
        firestore
            .collection(projectName)
            .document(eventId)
            .collection(CollectionName)
            .upsert(item.id, item)
    }

    fun delete(eventId: String, id: String) {
        firestore
            .collection(projectName)
            .document(eventId)
            .collection(CollectionName)
            .delete(id)
    }

    fun deleteDiff(eventId: String, ids: List<String>) {
        val diff = firestore
            .collection(projectName)
            .document(eventId)
            .collection(CollectionName)
            .diff<ScheduleDb>(ids)
            // Don't remove break items.
            .filter { it.talkId != null }
        firestore.batchDelete(
            diff.map {
                firestore
                    .collection(projectName)
                    .document(eventId)
                    .collection(CollectionName)
                    .document(it.id)
            }
        )
    }
}
