package com.paligot.confily.backend.schedules

import com.paligot.confily.backend.internals.helpers.database.Database
import com.paligot.confily.backend.internals.helpers.database.diff
import com.paligot.confily.backend.internals.helpers.database.get
import com.paligot.confily.backend.internals.helpers.database.getAll

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
        if (existing == null) {
            database.insert(
                eventId = eventId,
                collectionName = CollectionName,
                id = item.id,
                item = item
            )
        } else {
            database.update(eventId = eventId, collectionName = CollectionName, id = item.id, item = item)
        }
    }

    suspend fun delete(eventId: String, id: String) = database.delete(
        eventId = eventId,
        collectionName = CollectionName,
        id = id
    )

    suspend fun deleteDiff(eventId: String, ids: List<String>) {
        val diff = database.diff<ScheduleDb>(eventId, CollectionName, ids)
            // Don't remove break items.
            .filter { it.talkId != null }
        database.deleteAll(eventId, CollectionName, diff.map { it.id })
    }
}
