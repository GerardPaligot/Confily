package org.gdglille.devfest.database

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.gdglille.devfest.database.mappers.TalkMappers
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.models.SpeakerItemUi
import org.gdglille.devfest.models.SpeakerUi
import org.gdglille.devfest.models.TalkItemUi

class SpeakerDao(private val db: Conferences4HallDatabase, private val eventId: String) {
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
                talks = emptyList()
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
                return@combine speaker.copy(talks = talks)
            }
        )
    }

    private fun fetchTalksBySpeakerId(eventId: String, speakerId: String): Flow<List<TalkItemUi>> {
        val talkWithSpeakers = db.talkQueries.selectTalkWithSpeakersBySpeakerId(speakerId, eventId).executeAsList()
        return db.agendaQueries.selectScheduleItemsById(
            talkWithSpeakers.map { it.talk_id }, eventId, TalkMappers.talkItem
        ).asFlow().mapToList()
    }

    fun fetchSpeakers(eventId: String): Flow<List<SpeakerItemUi>> =
        db.speakerQueries.selectSpeakersByEvent(eventId, mapperItem).asFlow().mapToList()

    @Deprecated(message = "")
    fun fetchSpeaker(speakerId: String): Flow<SpeakerUi> = fetchSpeaker(eventId, speakerId)

    @Deprecated(message = "")
    fun fetchSpeakers(): Flow<List<SpeakerItemUi>> = fetchSpeakers(eventId)
}
