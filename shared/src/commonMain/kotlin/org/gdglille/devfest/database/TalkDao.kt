package org.gdglille.devfest.database

import org.gdglille.devfest.database.mappers.convertTalkUi
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.models.ui.TalkUi

class TalkDao(private val db: Conferences4HallDatabase) {
    fun fetchTalk(eventId: String, talkId: String): TalkUi = db.transactionWithResult {
        val talk = db.sessionQueries
            .selectSessionByTalkId(eventId, talkId)
            .executeAsOne()
        talk.convertTalkUi(
            db.sessionQueries
                .selectSpeakersByTalkId(event_id = eventId, talk_id = talkId)
                .executeAsList(),
            db.eventQueries
                .selectOpenfeedbackProjectId(eventId)
                .executeAsOne()
        )
    }
}
