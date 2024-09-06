package com.paligot.confily.backend.speakers

import com.paligot.confily.backend.internals.helpers.database.Database
import com.paligot.confily.backend.internals.helpers.database.get
import com.paligot.confily.backend.internals.helpers.database.getAll
import com.paligot.confily.backend.internals.helpers.database.query
import com.paligot.confily.backend.internals.helpers.database.whereIn
import com.paligot.confily.backend.internals.helpers.storage.Storage
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

private const val CollectionName = "speakers"

class SpeakerDao(private val database: Database, private val storage: Storage) {
    suspend fun get(eventId: String, id: String): SpeakerDb? = database.get(
        eventId = eventId,
        collectionName = CollectionName,
        id = id
    )

    suspend fun getByIds(eventId: String, ids: List<String>): List<SpeakerDb> =
        try {
            database.query(
                eventId = eventId,
                collectionName = CollectionName,
                "id".whereIn(ids)
            )
        } catch (ignored: Throwable) {
            emptyList()
        }

    suspend fun getAll(eventId: String): List<SpeakerDb> = database.getAll(
        eventId = eventId,
        collectionName = CollectionName
    )

    suspend fun insert(eventId: String, speaker: SpeakerDb) = coroutineScope {
        database.insert(eventId = eventId, collectionName = CollectionName, id = speaker.id, item = speaker)
    }

    suspend fun insertAll(eventId: String, speakers: List<SpeakerDb>) = coroutineScope {
        val asyncItems = speakers.map {
            async {
                database.insert(
                    eventId = eventId,
                    collectionName = CollectionName,
                    id = it.id,
                    item = it
                )
            }
        }
        asyncItems.awaitAll()
        Unit
    }

    suspend fun createOrUpdate(eventId: String, speaker: SpeakerDb): String = coroutineScope {
        if (speaker.id == "") {
            return@coroutineScope database.insert(
                eventId = eventId,
                collectionName = CollectionName
            ) { speaker.copy(id = it) }
        }
        val existing = database.get<SpeakerDb>(eventId = eventId, collectionName = CollectionName, id = speaker.id)
        if (existing == null) {
            database.insert(
                eventId = eventId,
                collectionName = CollectionName,
                id = speaker.id,
                item = speaker
            )
        } else {
            database.update(
                eventId = eventId,
                collectionName = CollectionName,
                id = speaker.id,
                item = speaker
            )
        }
        return@coroutineScope speaker.id
    }

    suspend fun saveProfile(eventId: String, id: String, content: ByteArray) = storage.upload(
        filename = "$eventId/speakers/$id.png",
        content = content
    )

    suspend fun deleteDiff(eventId: String, ids: List<String>) {
        val diff = database.diff(eventId, CollectionName, ids)
        database.deleteAll(eventId, CollectionName, diff)
    }
}
