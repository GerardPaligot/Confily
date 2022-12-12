package org.gdglille.devfest.backend.speakers

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.gdglille.devfest.backend.internals.helpers.database.Database
import org.gdglille.devfest.backend.internals.helpers.database.get
import org.gdglille.devfest.backend.internals.helpers.database.getAll
import org.gdglille.devfest.backend.internals.helpers.database.query
import org.gdglille.devfest.backend.internals.helpers.database.whereIn
import org.gdglille.devfest.backend.internals.helpers.storage.Storage

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
                collectionName = CollectionName,
                eventId = eventId,
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
        if (speaker.id == "") return@coroutineScope database.insert(
            eventId = eventId,
            collectionName = CollectionName
        ) { speaker.copy(id = it) }
        val existing = database.get<SpeakerDb>(eventId = eventId, collectionName = CollectionName, id = speaker.id)
        if (existing == null) database.insert(
            eventId = eventId,
            collectionName = CollectionName,
            id = speaker.id,
            item = speaker
        )
        else database.update(
            eventId = eventId,
            collectionName = CollectionName,
            id = speaker.id,
            item = speaker
        )
        return@coroutineScope speaker.id
    }

    suspend fun saveProfile(eventId: String, id: String, content: ByteArray) = storage.upload(
        filename = "$eventId/speakers/$id.png",
        content = content
    )
}
