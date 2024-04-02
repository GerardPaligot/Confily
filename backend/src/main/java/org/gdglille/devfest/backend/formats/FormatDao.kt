package org.gdglille.devfest.backend.formats

import org.gdglille.devfest.backend.internals.helpers.database.Database
import org.gdglille.devfest.backend.internals.helpers.database.get
import org.gdglille.devfest.backend.internals.helpers.database.getAll

private const val CollectionName = "formats"

class FormatDao(private val database: Database) {
    suspend fun get(eventId: String, id: String): FormatDb? = database.get(
        eventId = eventId,
        collectionName = CollectionName,
        id = id
    )

    suspend fun getAll(eventId: String): List<FormatDb> = database.getAll(
        eventId = eventId,
        collectionName = CollectionName
    )

    suspend fun createOrUpdate(eventId: String, item: FormatDb) {
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

    suspend fun deleteDiff(eventId: String, ids: List<String>) {
        val diff = database.diff(eventId, CollectionName, ids)
        database.deleteAll(eventId, CollectionName, diff)
    }
}
