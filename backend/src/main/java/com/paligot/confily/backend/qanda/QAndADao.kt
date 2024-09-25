package com.paligot.confily.backend.qanda

import com.paligot.confily.backend.internals.helpers.database.Database
import com.paligot.confily.backend.internals.helpers.database.get
import com.paligot.confily.backend.internals.helpers.database.query
import com.paligot.confily.backend.internals.helpers.database.whereEquals

private const val CollectionName = "qanda"

class QAndADao(private val database: Database) {
    suspend fun get(eventId: String, id: String): QAndADb? = database.get(
        eventId = eventId,
        collectionName = CollectionName,
        id = id
    )

    suspend fun getAll(eventId: String, language: String): List<QAndADb> = database.query(
        eventId = eventId,
        collectionName = CollectionName,
        "language".whereEquals(language)
    )

    suspend fun createOrUpdate(eventId: String, item: QAndADb) {
        if (item.id == null) {
            database.insert(
                eventId = eventId,
                collectionName = CollectionName
            ) { item.copy(id = it) }
        } else {
            database.update(
                eventId = eventId,
                collectionName = CollectionName,
                id = item.id,
                item = item
            )
        }
    }

    suspend fun hasQAndA(eventId: String): Boolean =
        database.count(eventId = eventId, collectionName = CollectionName) > 0

    suspend fun deleteDiff(eventId: String, ids: List<String>) {
        val diff = database.diff(eventId, CollectionName, ids)
        database.deleteAll(eventId, CollectionName, diff)
    }
}
