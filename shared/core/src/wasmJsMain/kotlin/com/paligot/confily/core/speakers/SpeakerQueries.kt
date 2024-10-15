package com.paligot.confily.core.speakers

import com.paligot.confily.core.combineAllSerializableScopedFlow
import com.paligot.confily.core.getAllSerializableScoped
import com.paligot.confily.core.getScopes
import com.paligot.confily.core.getSerializableScopedFlow
import com.paligot.confily.core.putSerializableScoped
import com.paligot.confily.core.removeScoped
import com.paligot.confily.core.speakers.SpeakerQueries.Scopes.SPEAKERS
import com.russhwolf.settings.ObservableSettings
import kotlinx.coroutines.flow.Flow

class SpeakerQueries(private val settings: ObservableSettings) {
    private object Scopes {
        const val SPEAKERS = "speakers"
    }

    fun upsertSpeaker(speaker: SpeakerDb) {
        settings.putSerializableScoped(SPEAKERS, speaker.id, speaker)
    }

    fun deleteSpeakers(ids: List<String>) {
        ids.forEach { settings.removeScoped(SPEAKERS, it) }
    }

    fun diffSpeakers(eventId: String, ids: List<String>): List<String> =
        settings.getScopes(SPEAKERS).filter { eventId == eventId && it !in ids }

    fun selectSpeaker(speakerId: String): Flow<SpeakerDb> = getSpeakerFlow(speakerId)

    fun selectSpeakersByEvent(eventId: String): Flow<List<SpeakerDb>> =
        settings.combineAllSerializableScopedFlow(SPEAKERS) { it.eventId == eventId }

    private fun getSpeakersFlow(): List<Flow<SpeakerDb>> =
        settings.getScopes(SPEAKERS).map { getSpeakerFlow(it) }

    fun getSpeakerFlow(speakerId: String): Flow<SpeakerDb> =
        settings.getSerializableScopedFlow(SPEAKERS, speakerId)

    fun getAllSpeakers(): List<SpeakerDb> = settings.getAllSerializableScoped(SPEAKERS)
}
