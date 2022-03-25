package com.paligot.conferences.backend.conferencehall

import com.paligot.conferences.backend.events.EventDao
import com.paligot.conferences.backend.events.convertToDb
import com.paligot.conferences.backend.network.ConferenceHallApi
import com.paligot.conferences.backend.speakers.SpeakerDao
import com.paligot.conferences.backend.speakers.convertToDb
import com.paligot.conferences.backend.talks.TalkDao
import com.paligot.conferences.backend.talks.convertToDb
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class ConferenceHallRepository(
    private val api: ConferenceHallApi,
    private val eventDao: EventDao,
    private val speakerDao: SpeakerDao,
    private val talkDao: TalkDao
) {
    suspend fun import(eventId: String, apiKey: String) = coroutineScope {
        val event = api.fetchEvent(eventId)
        val speakersAvatar = event.speakers
            .map {
                async {
                    try {
                        val avatar = api.fetchSpeakerAvatar(it.photoURL)
                        val bucketItem = speakerDao.saveProfile(eventId, it.uid, avatar)
                        it.uid to bucketItem.url
                    } catch (error: Throwable) {
                        it.uid to ""
                    }
                }
            }
            .awaitAll()
            .associate { it }
        speakerDao.insertAll(eventId, event.speakers.map { it.convertToDb(speakersAvatar[it.uid] ?: "") })
        talkDao.insertAll(eventId, event.talks.map { it.convertToDb(event.categories, event.formats) })
        eventDao.createOrUpdate(event.convertToDb(eventId, apiKey))
        return@coroutineScope event
    }
}