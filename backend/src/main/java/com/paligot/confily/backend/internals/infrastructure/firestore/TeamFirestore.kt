package com.paligot.confily.backend.internals.infrastructure.firestore

import com.google.cloud.firestore.Firestore
import com.paligot.confily.backend.internals.helpers.database.batchDelete
import com.paligot.confily.backend.internals.helpers.database.diffRefs
import com.paligot.confily.backend.internals.helpers.database.getDocument
import com.paligot.confily.backend.internals.helpers.database.getDocuments
import com.paligot.confily.backend.internals.helpers.database.insert
import com.paligot.confily.backend.internals.helpers.database.update

private const val CollectionName = "team-members"

class TeamFirestore(
    private val projectName: String,
    private val firestore: Firestore
) {
    fun get(eventId: String, id: String): TeamEntity? = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName)
        .getDocument(id)

    fun getAll(eventId: String): List<TeamEntity> = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName)
        .getDocuments()

    fun last(eventId: String): TeamEntity? = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName)
        .orderBy("order")
        .limitToLast(1)
        .getDocuments<TeamEntity>()
        .firstOrNull()

    fun createOrUpdate(eventId: String, item: TeamEntity): String {
        return if (item.id == "") {
            firestore
                .collection(projectName)
                .document(eventId)
                .collection(CollectionName)
                .insert { item.copy(id = it) }
        } else {
            firestore
                .collection(projectName)
                .document(eventId)
                .collection(CollectionName)
                .update(item.id, item)
            item.id
        }
    }

    fun deleteDiff(eventId: String, ids: List<String>) {
        val diff = firestore
            .collection(projectName)
            .document(eventId)
            .collection(CollectionName)
            .diffRefs(ids)
        firestore.batchDelete(diff)
    }
}
