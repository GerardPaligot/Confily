package org.gdglille.devfest.database

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.gdglille.devfest.database.mappers.SpeakerMappers
import org.gdglille.devfest.database.mappers.TalkMappers
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.models.SpeakerItemUi
import org.gdglille.devfest.models.SpeakerUi
import org.gdglille.devfest.models.TalkItemUi

class SpeakerDao(private val db: Conferences4HallDatabase) {
    fun fetchSpeaker(eventId: String, speakerId: String): Flow<SpeakerUi> {
        return combine(
            db.speakerQueries.selectSpeaker(speakerId, eventId, SpeakerMappers.speakerUi).asFlow().mapToOne(),
            fetchTalksBySpeakerId(eventId, speakerId),
            transform = { speaker, talks ->
                return@combine speaker.copy(talks = talks.toImmutableList())
            }
        )
    }

    private fun fetchTalksBySpeakerId(eventId: String, speakerId: String): Flow<ImmutableList<TalkItemUi>> {
        val talkWithSpeakers = db.talkQueries.selectTalkWithSpeakersBySpeakerId(speakerId, eventId).executeAsList()
        return db.agendaQueries.selectScheduleItemsById(
            talkWithSpeakers.map { it.talk_id }, eventId, TalkMappers.talkItem
        ).asFlow().mapToList().map { it.toImmutableList() }
    }

    fun fetchSpeakers(eventId: String): Flow<ImmutableList<SpeakerItemUi>> =
        db.speakerQueries.selectSpeakersByEvent(eventId, SpeakerMappers.speakerItemUi)
            .asFlow()
            .mapToList()
            .map { it.toImmutableList() }
}
