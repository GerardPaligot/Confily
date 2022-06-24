package org.gdglille.devfest.backend.conferencehall

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.gdglille.devfest.backend.events.EventDao
import org.gdglille.devfest.backend.events.convertToDb
import org.gdglille.devfest.backend.network.conferencehall.ConferenceHallApi
import org.gdglille.devfest.backend.speakers.SpeakerDao
import org.gdglille.devfest.backend.speakers.convertToDb
import org.gdglille.devfest.backend.talks.TalkDao
import org.gdglille.devfest.backend.talks.convertToDb

class ConferenceHallRepository(
    private val api: ConferenceHallApi,
    private val eventDao: EventDao,
    private val speakerDao: SpeakerDao,
    private val talkDao: TalkDao
) {
    suspend fun import(eventId: String, apiKey: String) = coroutineScope {
        val eventAccepted = api.fetchEventAccepted(eventId)
        val eventConfirmed = api.fetchEventConfirmed(eventId)
        val year = eventAccepted.conferenceDates.start.split("-")[0]
        val speakers = eventAccepted.speakers + eventConfirmed.speakers
        val talks = eventAccepted.talks + eventConfirmed.talks
        val speakersAvatar = speakers
            .filter { speaker -> speaker.photoURL != null }
            .map {
                async {
                    try {
                        val avatar = api.fetchSpeakerAvatar(it.photoURL!!)
                        val bucketItem = speakerDao.saveProfile(year, it.uid, avatar)
                        it.uid to bucketItem.url
                    } catch (error: Throwable) {
                        it.uid to ""
                    }
                }
            }
            .awaitAll()
            .associate { it }
        speakerDao.insertAll(year, speakers.map { it.convertToDb(speakersAvatar[it.uid] ?: "") })
        talkDao.insertAll(
            year,
            talks.map { it.convertToDb(eventAccepted.categories, eventAccepted.formats) }
        )
        eventDao.createOrUpdate(eventAccepted.convertToDb(year, eventId, apiKey))
        return@coroutineScope eventAccepted
    }
}
