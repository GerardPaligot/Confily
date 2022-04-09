package com.paligot.conferences.backend.speakers

import com.paligot.conferences.backend.database.Database
import com.paligot.conferences.backend.database.get
import com.paligot.conferences.backend.database.getAll
import com.paligot.conferences.backend.database.query
import com.paligot.conferences.backend.database.whereIn
import com.paligot.conferences.backend.storage.Storage
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class SpeakerDao(private val database: Database, private val storage: Storage) {
    suspend fun get(eventId: String, id: String): SpeakerDb? = database.get(eventId, id)

    suspend fun getByIds(eventId: String, vararg ids: String): List<SpeakerDb> =
        try {
            database.query(eventId, "id".whereIn(ids.toList()))
        } catch (ignored: Throwable) {
            emptyList()
        }

    suspend fun getAll(eventId: String): List<SpeakerDb> = database.getAll(eventId)

    suspend fun insertAll(eventId: String, speakers: List<SpeakerDb>) = coroutineScope {
        val asyncItems = speakers.map { async { database.insert(eventId, it.id, it) } }
        asyncItems.awaitAll()
        Unit
    }

    suspend fun createOrUpdate(eventId: String, speaker: SpeakerDb): String = coroutineScope {
        if (speaker.id == "") return@coroutineScope database.insert(eventId) { speaker.copy(id = it) }
        val existing = database.get<SpeakerDb>(eventId, speaker.id)
        if (existing == null) database.insert(eventId, speaker.id, speaker)
        else database.update(eventId, speaker.id, speaker)
        return@coroutineScope speaker.id
    }

    suspend fun saveProfile(eventId: String, id: String, content: ByteArray) = storage.upload(
        filename = "$eventId/speakers/$id.png",
        content = content
    )
}
