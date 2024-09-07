package com.paligot.confily.core.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import cafe.adriel.lyricist.Lyricist
import com.paligot.confily.core.database.mappers.convertEventSessionItemUi
import com.paligot.confily.core.database.mappers.convertTalkUi
import com.paligot.confily.db.ConfilyDatabase
import com.paligot.confily.models.ui.EventSessionItemUi
import com.paligot.confily.models.ui.TalkUi
import com.paligot.confily.resources.Strings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

class TalkDao(
    private val db: ConfilyDatabase,
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

    fun fetchEventSession(eventId: String, sessionId: String): Flow<EventSessionItemUi> =
        db.sessionQueries.selectEventSessionById(event_id = eventId, session_event_id = sessionId)
            .asFlow()
            .mapToOne(dispatcher)
            .map { it.convertEventSessionItemUi(lyricist.strings) }
}
