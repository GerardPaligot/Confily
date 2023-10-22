package org.gdglille.devfest.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.gdglille.devfest.Platform
import org.gdglille.devfest.database.mappers.SpeakerMappers
import org.gdglille.devfest.database.mappers.convertTalkItemUi
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.models.ui.SpeakerItemUi
import org.gdglille.devfest.models.ui.SpeakerUi
import org.gdglille.devfest.models.ui.TalkItemUi

class SpeakerDao(
    private val db: Conferences4HallDatabase,
    private val platform: Platform
) {
    fun fetchSpeaker(eventId: String, speakerId: String): Flow<SpeakerUi> {
        return combine(
            db.speakerQueries.selectSpeaker(speakerId, eventId, SpeakerMappers.speakerUi)
                .asFlow()
                .mapToOne(Dispatchers.IO),
            fetchTalksBySpeakerId(eventId, speakerId),
            transform = { speaker, talks ->
                return@combine speaker.copy(talks = talks.toImmutableList())
            }
        )
    }

    private fun fetchTalksBySpeakerId(
        eventId: String,
        speakerId: String
    ): Flow<ImmutableList<TalkItemUi>> = db.transactionWithResult {
        db.sessionQueries
            .selectTalksBySpeakerId(eventId, speakerId)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { talks ->
                talks
                    .map { talk ->
                        talk.convertTalkItemUi(
                            getString = platform::getString,
                            getStringArg = platform::getString,
                            getPluralsArg = platform::getString,
                            session = db.sessionQueries
                                .selectSessionByTalkId(eventId, talk.id)
                                .executeAsOne(),
                            speakers = db.sessionQueries
                                .selectSpeakersByTalkId(eventId, talk.id)
                                .executeAsList()
                        )
                    }
                    .toImmutableList()
            }
    }

    fun fetchSpeakers(eventId: String): Flow<ImmutableList<SpeakerItemUi>> =
        db.speakerQueries.selectSpeakersByEvent(eventId, SpeakerMappers.speakerItemUi)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { it.toImmutableList() }
}
