package org.gdglille.devfest.database

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import org.gdglille.devfest.database.mappers.TalkMappers
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.models.SpeakerItemUi
import org.gdglille.devfest.models.SpeakerUi
import org.gdglille.devfest.models.TalkItemUi

class SpeakerDao(private val db: Conferences4HallDatabase) {
    private val mapper =
        { _: String, display_name: String, bio: String, company: String?, photo_url: String, twitter: String?, github: String?, _: String ->
            SpeakerUi(
                name = display_name,
                bio = bio,
                company = company ?: "",
                url = photo_url,
                twitter = twitter?.split("twitter.com/")?.get(1),
                twitterUrl = twitter,
                github = github?.split("github.com/")?.get(1),
                githubUrl = github,
                talks = persistentListOf()
            )
        }
    private val mapperItem =
        { id: String, display_name: String, _: String, company: String?, photo_url: String, twitter: String?, _: String?, _: String ->
            SpeakerItemUi(
                id = id,
                name = display_name,
                company = company ?: "",
                url = photo_url,
                twitter = twitter?.split("twitter.com/")?.get(1)
            )
        }

    fun fetchSpeaker(eventId: String, speakerId: String): Flow<SpeakerUi> {
        return combine(
            db.speakerQueries.selectSpeaker(speakerId, eventId, mapper).asFlow().mapToOne(),
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
        db.speakerQueries.selectSpeakersByEvent(eventId, mapperItem)
            .asFlow()
            .mapToList()
            .map { it.toImmutableList() }
}
