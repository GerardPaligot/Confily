package org.gdglille.devfest.backend.talks

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.gdglille.devfest.backend.internals.helpers.database.Database
import org.gdglille.devfest.backend.internals.helpers.database.get
import org.gdglille.devfest.backend.internals.helpers.database.getAll

private const val CollectionName = "talks"

class TalkDao(private val database: Database) {
    suspend fun get(eventId: String, id: String): TalkDb? = database.get(
        eventId = eventId,
        collectionName = CollectionName,
        id = id
    )

    suspend fun getAll(eventId: String): List<TalkDb> = database.getAll(
        eventId = eventId,
        collectionName = CollectionName
    )

    suspend fun insert(eventId: String, talk: TalkDb) {
        database.insert(
            eventId = eventId,
            collectionName = CollectionName,
            id = talk.id,
            item = talk
        )
    }

    suspend fun insertAll(eventId: String, talks: List<TalkDb>) = coroutineScope {
        val asyncItems = talks.map {
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

    suspend fun createOrUpdate(eventId: String, talk: TalkDb): String = coroutineScope {
        if (talk.id == "") return@coroutineScope database.insert(
            eventId = eventId,
            collectionName = CollectionName
        ) { talk.copy(id = it) }
        val existing = database.get<TalkDb>(eventId = eventId, collectionName = CollectionName, id = talk.id)
        if (existing == null) database.insert(
            eventId = eventId,
            collectionName = CollectionName,
            id = talk.id,
            item = talk
        )
        else database.update(eventId = eventId, collectionName = CollectionName, id = talk.id, item = talk)
        return@coroutineScope talk.id
    }
}
