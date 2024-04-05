package org.gdglille.devfest.backend.categories

import org.gdglille.devfest.backend.internals.helpers.database.Database
import org.gdglille.devfest.backend.internals.helpers.database.get
import org.gdglille.devfest.backend.internals.helpers.database.getAll

private const val CollectionName = "categories"

class CategoryDao(private val database: Database) {
    suspend fun get(eventId: String, id: String): CategoryDb? = database.get(
        eventId = eventId,
        collectionName = CollectionName,
        id = id
    )

    suspend fun getAll(eventId: String): List<CategoryDb> = database.getAll(
        eventId = eventId,
        collectionName = CollectionName
    )

    suspend fun createOrUpdate(eventId: String, item: CategoryDb) {
        if (item.id == null) database.insert(
            eventId = eventId,
            collectionName = CollectionName
        ) { item.copy(id = it) }
        else database.update(
            eventId = eventId,
            collectionName = CollectionName,
            id = item.id,
            item = item
        )
    }

    suspend fun deleteDiff(eventId: String, ids: List<String>) {
        val diff = database.diff(eventId, CollectionName, ids)
        database.deleteAll(eventId, CollectionName, diff)
    }
}
