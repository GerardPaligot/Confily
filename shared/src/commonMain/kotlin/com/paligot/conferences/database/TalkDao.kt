package com.paligot.conferences.database

import com.paligot.conferences.db.Conferences4HallDatabase
import com.paligot.conferences.models.SpeakerItemUi
import com.paligot.conferences.models.TalkUi

class TalkDao(private val db: Conferences4HallDatabase) {
    fun fetchTalk(talkId: String): TalkUi = db.talkQueries.transactionWithResult {
        val talkWithSpeakers = db.talkQueries.selectTalkWithSpeakersByTalkId(talkId).executeAsList()
        val speakers = db.speakerQueries.selectSpeakers(talkWithSpeakers.map { it.speaker_id }).executeAsList()
        val talk = db.talkQueries.selectTalk(talkId).executeAsOne()
        return@transactionWithResult TalkUi(
            title = talk.title,
            level = talk.level,
            abstract = talk.abstract_,
            startTime = talk.start_time,
            endTime = talk.end_time,
            room = talk.room,
            speakers = speakers.map {
                SpeakerItemUi(
                    id = it.id,
                    name = it.display_name,
                    company = it.company ?: "",
                    url = it.photo_url,
                    twitter = it.twitter?.split("twitter.com/")?.get(1)
                )
            }
        )
    }
}