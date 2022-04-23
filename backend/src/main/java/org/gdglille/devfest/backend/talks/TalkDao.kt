package org.gdglille.devfest.backend.talks

import org.gdglille.devfest.backend.database.Database
import org.gdglille.devfest.backend.database.get
import org.gdglille.devfest.backend.database.getAll
import org.gdglille.devfest.backend.speakers.SpeakerDb
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class TalkDao(private val database: Database) {
  suspend fun get(eventId: String, id: String): TalkDb? = database.get(eventId, id)

  suspend fun getAll(eventId: String): List<TalkDb> = database.getAll(eventId)

  suspend fun insertAll(eventId: String, talks: List<TalkDb>) = coroutineScope {
    val asyncItems = talks.map { async { database.insert(eventId, it.id, it) } }
    asyncItems.awaitAll()
    Unit
  }

  suspend fun createOrUpdate(eventId: String, talk: TalkDb): String = coroutineScope {
    if (talk.id == "") return@coroutineScope database.insert(eventId) { talk.copy(id = it) }
    val existing = database.get<SpeakerDb>(eventId, talk.id)
    if (existing == null) database.insert(eventId, talk.id, talk)
    else database.update(eventId, talk.id, talk)
    return@coroutineScope talk.id
  }
}
