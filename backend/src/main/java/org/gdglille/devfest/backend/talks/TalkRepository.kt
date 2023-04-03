package org.gdglille.devfest.backend.talks

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.gdglille.devfest.backend.NotFoundException
import org.gdglille.devfest.backend.events.EventDao
import org.gdglille.devfest.backend.speakers.SpeakerDao
import org.gdglille.devfest.backend.speakers.convertToModel
import org.gdglille.devfest.models.inputs.TalkInput

class TalkRepository(
    private val eventDao: EventDao,
    private val speakerDao: SpeakerDao,
    private val talkDao: TalkDao
) {
    suspend fun list(eventId: String) = coroutineScope {
        val eventDb = eventDao.get(eventId) ?: throw NotFoundException("Event $eventId Not Found")
        val talks = talkDao.getAll(eventId)
        val speakers = speakerDao.getAll(eventId)
        val asyncItems = talks.map { talkDb ->
            async {
                talkDb.convertToModel(
                    speakers.filter { talkDb.speakerIds.contains(it.id) }.map { it.convertToModel() },
                    eventDb
                )
            }
        }
        return@coroutineScope asyncItems.awaitAll()
    }

    suspend fun create(eventId: String, apiKey: String, talkInput: TalkInput) = coroutineScope {
        val event = eventDao.getVerified(eventId, apiKey)
        val talkDb = talkInput.convertToDb()
        val id = talkDao.createOrUpdate(eventId, talkDb)
        eventDao.updateAgendaUpdatedAt(event)
        return@coroutineScope id
    }

    suspend fun get(eventId: String, talkId: String) = coroutineScope {
        val eventDb = eventDao.get(eventId) ?: throw NotFoundException("Event $eventId Not Found")
        val talk = talkDao.get(eventId, talkId) ?: throw NotFoundException("Talk $talkId Not Found")
        return@coroutineScope talk.convertToModel(
            speakerDao.getByIds(eventId, talk.speakerIds).map { it.convertToModel() }, eventDb
        )
    }

    suspend fun update(eventId: String, apiKey: String, talkId: String, talkInput: TalkInput) =
        coroutineScope {
            val event = eventDao.getVerified(eventId, apiKey)
            talkDao.createOrUpdate(eventId, talkInput.convertToDb(id = talkId))
            eventDao.updateAgendaUpdatedAt(event)
            return@coroutineScope talkId
        }
}
