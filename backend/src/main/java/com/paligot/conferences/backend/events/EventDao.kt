package com.paligot.conferences.backend.events

import com.paligot.conferences.backend.database.Database
import com.paligot.conferences.backend.database.get
import com.paligot.conferences.backend.database.getAll

class EventDao(private val database: Database) {
    suspend fun get(id: String): EventDb? = database.get(id)

    suspend fun createOrUpdate(event: EventDb) {
        val existing = database.get<EventDb>(event.id)
        if (existing == null) database.insert(event.id, event)
        else database.update(event.id, event)
    }
}
