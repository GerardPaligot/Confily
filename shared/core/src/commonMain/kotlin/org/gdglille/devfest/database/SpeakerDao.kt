package org.gdglille.devfest.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import cafe.adriel.lyricist.Lyricist
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.gdglille.devfest.database.mappers.convertTalkItemUi
import org.gdglille.devfest.database.mappers.convertToSpeakerItemUi
import org.gdglille.devfest.database.mappers.convertToSpeakerUi
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.models.ui.SpeakerItemUi
import org.gdglille.devfest.models.ui.SpeakerUi
import org.gdglille.devfest.models.ui.TalkItemUi
import kotlin.coroutines.CoroutineContext
import org.gdglille.devfest.android.shared.resources.Strings

class SpeakerDao(
    private val db: Conferences4HallDatabase,
    private val lyricist: Lyricist<Strings>,
    private val dispatcher: CoroutineContext
) {
    fun fetchSpeaker(eventId: String, speakerId: String): Flow<SpeakerUi> {
        return combine(
            db.speakerQueries.selectSpeaker(speakerId, eventId)
                .asFlow()
                .mapToOne(dispatcher),
            fetchTalksBySpeakerId(eventId, speakerId),
            transform = { speaker, talks ->
                return@combine speaker.convertToSpeakerUi(
                    talks = talks.toImmutableList(),
                    strings = lyricist.strings
                )
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
            .mapToList(dispatcher)
            .map { talks ->
                talks
                    .map { talk ->
                        talk.convertTalkItemUi(
                            session = db.sessionQueries
                                .selectSessionByTalkId(eventId, talk.id)
                                .executeAsOne(),
                            speakers = db.sessionQueries
                                .selectSpeakersByTalkId(eventId, talk.id)
                                .executeAsList(),
                            strings = lyricist.strings
                        )
                    }
                    .toImmutableList()
            }
    }

    fun fetchSpeakers(eventId: String): Flow<ImmutableList<SpeakerItemUi>> =
        db.speakerQueries.selectSpeakersByEvent(eventId)
            .asFlow()
            .mapToList(dispatcher)
            .map { speakers ->
                speakers
                    .map { speaker -> speaker.convertToSpeakerItemUi(lyricist.strings) }
                    .toImmutableList()
            }
}
