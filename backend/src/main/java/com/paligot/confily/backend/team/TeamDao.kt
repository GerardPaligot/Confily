package com.paligot.confily.backend.team

import com.google.cloud.firestore.Firestore
import com.paligot.confily.backend.internals.helpers.database.batchDelete
import com.paligot.confily.backend.internals.helpers.database.diffRefs
import com.paligot.confily.backend.internals.helpers.database.getDocument
import com.paligot.confily.backend.internals.helpers.database.getDocuments
import com.paligot.confily.backend.internals.helpers.database.insert
import com.paligot.confily.backend.internals.helpers.database.update
import com.paligot.confily.backend.internals.helpers.storage.MimeType
import com.paligot.confily.backend.internals.helpers.storage.Storage

private const val CollectionName = "team-members"

class TeamDao(
    private val projectName: String,
    private val firestore: Firestore,
    private val storage: Storage
) {
    fun get(eventId: String, id: String): TeamDb? = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName)
        .getDocument(id)

    fun getAll(eventId: String): List<TeamDb> = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName)
        .getDocuments()

    fun createOrUpdate(eventId: String, item: TeamDb): String {
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

    suspend fun saveTeamPicture(eventId: String, id: String, content: ByteArray, mimeType: MimeType) =
        storage.upload(
            filename = "$eventId/team/$id.${mimeType.extension}",
            content = content,
            mimeType = mimeType
        )

    fun deleteDiff(eventId: String, ids: List<String>) {
        val diff = firestore
            .collection(projectName)
            .document(eventId)
            .collection(CollectionName)
            .diffRefs(ids)
        firestore.batchDelete(diff)
    }
}
