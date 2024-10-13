package com.paligot.confily.core.speakers

import cafe.adriel.lyricist.Lyricist
import com.paligot.confily.core.schedules.SessionQueries
import com.paligot.confily.core.schedules.convertTalkItemUi
import com.paligot.confily.models.ui.SpeakerItemUi
import com.paligot.confily.models.ui.SpeakerUi
import com.paligot.confily.models.ui.TalkItemUi
import com.paligot.confily.resources.Strings
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class SpeakerDaoSettings(
    private val sessionQueries: SessionQueries,
    private val speakerQueries: SpeakerQueries,
    private val lyricist: Lyricist<Strings>
) : SpeakerDao {
    override fun fetchSpeaker(eventId: String, speakerId: String): Flow<SpeakerUi> = combine(
        flow = speakerQueries.selectSpeaker(speakerId),
        flow2 = fetchTalksBySpeakerId(eventId, speakerId),
        transform = { speaker, talks ->
            speaker.convertToSpeakerUi(
                talks = talks.toImmutableList(),
                strings = lyricist.strings
            )
        }
    )

    private fun fetchTalksBySpeakerId(
        eventId: String,
        speakerId: String
    ): Flow<ImmutableList<TalkItemUi>> = sessionQueries
        .selectTalksBySpeakerId(eventId, speakerId)
        .map { talks ->
            talks
                .map { talk ->
                    talk.convertTalkItemUi(
                        session = sessionQueries
                            .getSessionByTalkId(eventId, talk.session.id),
                        speakers = sessionQueries
                            .getSpeakersByTalkId(eventId, talk.session.id),
                        strings = lyricist.strings
                    )
                }
                .toImmutableList()
        }

    override fun fetchSpeakers(eventId: String): Flow<ImmutableList<SpeakerItemUi>> =
        speakerQueries.selectSpeakersByEvent(eventId)
            .map { speakers ->
                speakers
                    .map { speaker -> speaker.convertSpeakerItemUi(lyricist.strings) }
                    .toImmutableList()
            }
}
