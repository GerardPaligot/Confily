package com.paligot.conferences.database

import com.paligot.conferences.db.Conferences4HallDatabase
import com.paligot.conferences.models.SpeakerUi

class SpeakerDao(private val db: Conferences4HallDatabase) {
    private val mapper =
        { _: String, display_name: String, bio: String, company: String?, photo_url: String, twitter: String?, github: String? ->
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

    fun fetchSpeaker(speakerId: String): SpeakerUi = db.speakerQueries.selectSpeaker(speakerId, mapper).executeAsOne()
}
