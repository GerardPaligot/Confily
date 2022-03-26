package com.paligot.conferences.backend.events

import com.paligot.conferences.backend.NotAuthorized
import com.paligot.conferences.backend.NotFoundException
import com.paligot.conferences.backend.database.Database
import com.paligot.conferences.backend.database.get

class EventDao(private val database: Database) {
    suspend fun get(id: String): EventDb? = database.get(id)
    suspend fun getVerified(id: String, apiKey: String?): EventDb {
        val eventDb = database.get<EventDb>(id) ?: throw NotFoundException("Event $id Not Found")
        return if (eventDb.apiKey == apiKey) eventDb else throw NotAuthorized
    }

    suspend fun createOrUpdate(event: EventDb) {
        val existing = database.get<EventDb>(event.year)
        if (existing == null) database.insert(event.year, event)
        else database.update(event.year, event)
    }

    suspend fun updateUpdatedAt(id: String) {
        val existing = database.get<EventDb>(id) ?: return
        database.update(id, existing.copy(updatedAt = System.currentTimeMillis()))
    }
}
