package com.paligot.conferences.backend.events

import com.paligot.conferences.backend.database.Database
import com.paligot.conferences.backend.database.get

class EventDao(private val database: Database) {
    suspend fun get(id: String): EventDb? = database.get(id)

    suspend fun createOrUpdate(event: EventDb) {
        val existing = database.get<EventDb>(event.id)
        if (existing == null) database.insert(event.id, event)
        else database.update(event.id, event)
    }

    suspend fun updateUpdatedAt(id: String) {
        val existing = database.get<EventDb>(id) ?: return
        database.update(id, existing.copy(updatedAt = System.currentTimeMillis()))
    }
}
