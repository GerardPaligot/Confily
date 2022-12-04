package org.gdglille.devfest.backend.schedulers

import org.gdglille.devfest.backend.internals.helpers.database.Database
import org.gdglille.devfest.backend.internals.helpers.database.get
import org.gdglille.devfest.backend.internals.helpers.database.getAll

private const val CollectionName = "schedule-items"

class ScheduleItemDao(private val database: Database) {
    suspend fun get(eventId: String, id: String): ScheduleDb? = database.get(
        eventId = eventId,
        collectionName = CollectionName,
        id = id
    )

    suspend fun getAll(eventId: String): List<ScheduleDb> = database.getAll(
        eventId = eventId,
        collectionName = CollectionName
    )

    suspend fun createOrUpdate(eventId: String, item: ScheduleDb) {
        val existing = database.get<ScheduleDb>(eventId = eventId, collectionName = CollectionName, id = item.id)
        if (existing == null) database.insert(
            eventId = eventId,
            collectionName = CollectionName,
            id = item.id,
            item = item
        )
        else database.update(eventId = eventId, collectionName = CollectionName, id = item.id, item = item)
    }

    suspend fun delete(eventId: String, id: String) = database.delete(
        eventId = eventId,
        collectionName = CollectionName,
        id = id
    )
}
