package org.gdglille.devfest.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import cafe.adriel.lyricist.Lyricist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.gdglille.devfest.database.mappers.convertTalkUi
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.models.ui.TalkUi
import kotlin.coroutines.CoroutineContext
import org.gdglille.devfest.android.shared.resources.Strings

class TalkDao(
    private val db: Conferences4HallDatabase,
    private val lyricist: Lyricist<Strings>,
    private val dispatcher: CoroutineContext
) {
    fun fetchTalk(eventId: String, talkId: String): Flow<TalkUi> = db.transactionWithResult {
        combine(
            flow = db.sessionQueries
                .selectSpeakersByTalkId(event_id = eventId, talk_id = talkId)
                .asFlow()
                .mapToList(dispatcher),
            flow2 = db.eventQueries
                .selectOpenfeedbackProjectId(eventId)
                .asFlow()
                .mapToOne(dispatcher),
            flow3 = db.sessionQueries
                .selectSessionByTalkId(eventId, talkId)
                .asFlow()
                .mapToOne(dispatcher),
            transform = { speakers, openfeedback, talk ->
                talk.convertTalkUi(speakers, openfeedback, lyricist.strings)
            }
        )
    }
}
