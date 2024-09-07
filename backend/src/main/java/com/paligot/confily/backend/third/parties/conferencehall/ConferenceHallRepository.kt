package com.paligot.confily.backend.third.parties.conferencehall

import com.paligot.confily.backend.NotAcceptableException
import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.categories.CategoryDao
import com.paligot.confily.backend.events.EventDao
import com.paligot.confily.backend.formats.FormatDao
import com.paligot.confily.backend.internals.CommonApi
import com.paligot.confily.backend.internals.slug
import com.paligot.confily.backend.sessions.SessionDao
import com.paligot.confily.backend.speakers.SpeakerDao
import com.paligot.confily.backend.speakers.convertToDb
import com.paligot.confily.models.inputs.conferencehall.ImportTalkInput
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

@Suppress("LongParameterList")
class ConferenceHallRepository(
    private val api: ConferenceHallApi,
    private val commonApi: CommonApi,
    private val eventDao: EventDao,
    private val speakerDao: SpeakerDao,
    private val sessionDao: SessionDao,
    private val categoryDao: CategoryDao,
    private val formatDao: FormatDao
) {
    suspend fun importTalks(
        eventId: String,
        apiKey: String,
        input: ImportTalkInput
    ) = coroutineScope {
        val event = eventDao.getVerified(eventId, apiKey)
        val chConfig = event.conferenceHallConfig
            ?: throw NotAcceptableException("ConferenceHall config not initialized")
        val eventConfirmed = api.fetchEventConfirmed(chConfig.eventId, chConfig.apiKey)
        eventConfirmed.categories
            .map { async { categoryDao.createOrUpdate(eventId, it.convertToDb(input.categories)) } }
            .awaitAll()
        eventConfirmed.formats
            .map { async { formatDao.createOrUpdate(eventId, it.convertToDb(input.formats)) } }
            .awaitAll()
        sessionDao.insertAll(eventId, eventConfirmed.talks.map { it.convertToDb() })
    }

    suspend fun importTalk(
        eventId: String,
        apiKey: String,
        talkId: String,
        input: ImportTalkInput
    ) = coroutineScope {
        val event = eventDao.getVerified(eventId, apiKey)
        val chConfig = event.conferenceHallConfig
            ?: throw NotAcceptableException("ConferenceHall config not initialized")
        val eventConfirmed = api.fetchEventConfirmed(chConfig.eventId, chConfig.apiKey)
        val talk = eventConfirmed.talks.find { it.id == talkId }
            ?: throw NotFoundException("Talk $talkId not found in confirmed talks")
        eventConfirmed.categories
            .map { async { categoryDao.createOrUpdate(eventId, it.convertToDb(input.categories)) } }
            .awaitAll()
        eventConfirmed.formats
            .map { async { formatDao.createOrUpdate(eventId, it.convertToDb(input.formats)) } }
            .awaitAll()
        sessionDao.insert(eventId, talk.convertToDb())
    }

    suspend fun importSpeakers(eventId: String, apiKey: String) = coroutineScope {
        val event = eventDao.getVerified(eventId, apiKey)
        val chConfig = event.conferenceHallConfig
            ?: throw NotAcceptableException("ConferenceHall config not initialized")
        val eventConfirmed = api.fetchEventConfirmed(chConfig.eventId, chConfig.apiKey)
        val speakersAvatar = eventConfirmed.speakers
            .filter { speaker -> speaker.photoURL != null }
            .map {
                async {
                    try {
                        val avatar = commonApi.fetchByteArray(it.photoURL!!)
                        val bucketItem = speakerDao.saveProfile(eventId, it.uid, avatar)
                        it.uid to bucketItem.url
                    } catch (ignored: Throwable) {
                        it.uid to ""
                    }
                }
            }
            .awaitAll()
            .associate { it }
        speakerDao.insertAll(
            eventConfirmed.name.slug(),
            eventConfirmed.speakers.map { it.convertToDb(speakersAvatar[it.uid] ?: "") }
        )
    }

    suspend fun importSpeaker(eventId: String, apiKey: String, speakerId: String) {
        val event = eventDao.getVerified(eventId, apiKey)
        val chConfig = event.conferenceHallConfig
            ?: throw NotAcceptableException("ConferenceHall config not initialized")
        val eventConfirmed = api.fetchEventConfirmed(chConfig.eventId, chConfig.apiKey)
        val speaker = eventConfirmed.speakers.find { it.uid == speakerId }
            ?: throw NotFoundException("Speaker $speakerId not found in confirmed talks")
        if (speaker.photoURL == null) throw NotAcceptableException("Speaker $speakerId doesn't have a photo")
        val avatar = commonApi.fetchByteArray(speaker.photoURL)
        val bucketItem = speakerDao.saveProfile(eventId, speaker.uid, avatar)
        speakerDao.insert(eventId, speaker.convertToDb(bucketItem.url))
    }
}
