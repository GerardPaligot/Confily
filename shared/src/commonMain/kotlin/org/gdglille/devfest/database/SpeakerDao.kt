package org.gdglille.devfest.database

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.models.SpeakerItemUi
import org.gdglille.devfest.models.SpeakerUi

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
                githubUrl = github
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

    fun fetchSpeaker(speakerId: String): SpeakerUi = db.speakerQueries.selectSpeaker(speakerId, mapper).executeAsOne()
    fun fetchSpeakers(): Flow<List<SpeakerItemUi>> =
        db.speakerQueries.selectSpeakersByEvent(eventId, mapperItem).asFlow().mapToList()
}
