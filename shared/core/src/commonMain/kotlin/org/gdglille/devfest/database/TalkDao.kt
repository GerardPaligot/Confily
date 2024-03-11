package org.gdglille.devfest.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.gdglille.devfest.database.mappers.convertTalkUi
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.models.ui.TalkUi

class TalkDao(private val db: Conferences4HallDatabase) {
    fun fetchTalk(eventId: String, talkId: String): Flow<TalkUi> = db.transactionWithResult {
        combine(
            flow = db.sessionQueries
                .selectSpeakersByTalkId(event_id = eventId, talk_id = talkId)
                .asFlow()
                .mapToList(Dispatchers.IO),
            flow2 = db.eventQueries
                .selectOpenfeedbackProjectId(eventId)
                .asFlow()
                .mapToOne(Dispatchers.IO),
            flow3 = db.sessionQueries
                .selectSessionByTalkId(eventId, talkId)
                .asFlow()
                .mapToOne(Dispatchers.IO),
            transform = { speakers, openfeedback, talk ->
                talk.convertTalkUi(speakers, openfeedback)
            }
        )
    }
}
