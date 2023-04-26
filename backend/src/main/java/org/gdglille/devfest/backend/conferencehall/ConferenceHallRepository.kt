package org.gdglille.devfest.backend.conferencehall

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.gdglille.devfest.backend.NotAcceptableException
import org.gdglille.devfest.backend.NotFoundException
import org.gdglille.devfest.backend.events.EventDao
import org.gdglille.devfest.backend.internals.network.conferencehall.ConferenceHallApi
import org.gdglille.devfest.backend.internals.slug
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
    suspend fun importTalks(eventId: String, apiKey: String) {
        val event = eventDao.getVerified(eventId, apiKey)
        val chConfig = event.conferenceHallConfig
            ?: throw NotAcceptableException("ConferenceHall config not initialized")
        val eventAccepted = api.fetchEventAccepted(chConfig.eventId, chConfig.apiKey)
        val eventConfirmed = api.fetchEventConfirmed(chConfig.eventId, chConfig.apiKey)
        val talks = eventAccepted.talks + eventConfirmed.talks
        talkDao.insertAll(
            eventAccepted.name.slug(),
            talks.map { it.convertToDb(eventAccepted.categories, eventAccepted.formats) }
        )
    }

    suspend fun importTalk(eventId: String, apiKey: String, talkId: String) {
        val event = eventDao.getVerified(eventId, apiKey)
        val chConfig = event.conferenceHallConfig
            ?: throw NotAcceptableException("ConferenceHall config not initialized")
        val eventConfirmed = api.fetchEventConfirmed(chConfig.eventId, chConfig.apiKey)
        val talk = eventConfirmed.talks.find { it.id == talkId }
            ?: throw NotFoundException("Talk $talkId not found in confirmed talks")
        talkDao.insert(eventId, talk.convertToDb(eventConfirmed.categories, eventConfirmed.formats))
    }

    suspend fun importSpeakers(eventId: String, apiKey: String) = coroutineScope {
        val event = eventDao.getVerified(eventId, apiKey)
        val chConfig = event.conferenceHallConfig
            ?: throw NotAcceptableException("ConferenceHall config not initialized")
        val eventAccepted = api.fetchEventAccepted(chConfig.eventId, chConfig.apiKey)
        val eventConfirmed = api.fetchEventConfirmed(chConfig.eventId, chConfig.apiKey)
        val speakers = eventAccepted.speakers + eventConfirmed.speakers
        val speakersAvatar = speakers
            .filter { speaker -> speaker.photoURL != null }
            .map {
                async {
                    try {
                        val avatar = api.fetchSpeakerAvatar(it.photoURL!!)
                        val bucketItem = speakerDao.saveProfile(eventId, it.uid, avatar)
                        it.uid to bucketItem.url
                    } catch (ignored: Throwable) {
                        it.uid to ""
                    }
                }
            }
            .awaitAll()
            .associate { it }
        speakerDao.insertAll(eventAccepted.name.slug(), speakers.map { it.convertToDb(speakersAvatar[it.uid] ?: "") })
    }

    suspend fun importSpeaker(eventId: String, apiKey: String, speakerId: String) {
        val event = eventDao.getVerified(eventId, apiKey)
        val chConfig = event.conferenceHallConfig
            ?: throw NotAcceptableException("ConferenceHall config not initialized")
        val eventConfirmed = api.fetchEventConfirmed(chConfig.eventId, chConfig.apiKey)
        val speaker = eventConfirmed.speakers.find { it.uid == speakerId }
            ?: throw NotFoundException("Speaker $speakerId not found in confirmed talks")
        if (speaker.photoURL == null) throw NotAcceptableException("Speaker $speakerId doesn't have a photo")
        val avatar = api.fetchSpeakerAvatar(speaker.photoURL)
        val bucketItem = speakerDao.saveProfile(eventId, speaker.uid, avatar)
        speakerDao.insert(eventId, speaker.convertToDb(bucketItem.url))
    }
}
