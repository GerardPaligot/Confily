package com.paligot.conferences.backend.schedulers

import com.paligot.conferences.backend.database.Database
import com.paligot.conferences.backend.database.get
import com.paligot.conferences.backend.database.getAll

class ScheduleItemDao(private val database: Database) {
    suspend fun get(eventId: String, id: String): ScheduleDb? = database.get(eventId, id)
    suspend fun getAll(eventId: String): List<ScheduleDb> = database.getAll(eventId)

    suspend fun createOrUpdate(eventId: String, item: ScheduleDb) {
        val existing = database.get<ScheduleDb>(eventId, item.id)
        if (existing == null) database.insert(eventId, item.id, item)
        else database.update(eventId, item.id, item)
    }

    suspend fun delete(eventId: String, id: String) = database.delete(eventId, id)
}
