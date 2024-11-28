package com.paligot.confily.core.speakers

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.paligot.confily.core.speakers.entities.SpeakerInfo
import com.paligot.confily.core.speakers.entities.SpeakerItem
import com.paligot.confily.db.ConfilyDatabase
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class SpeakerDaoSQLDelight(
    private val db: ConfilyDatabase,
    private val dispatcher: CoroutineContext
) : SpeakerDao {
    override fun fetchSpeaker(eventId: String, speakerId: String): Flow<SpeakerInfo> =
        db.speakerQueries
            .selectSpeaker(speakerId, eventId, speakerInfoMapper)
            .asFlow()
            .mapToOne(dispatcher)

    override fun fetchSpeakers(eventId: String): Flow<List<SpeakerItem>> =
        db.speakerQueries
            .selectSpeakersByEvent(eventId, speakerItemMapper)
            .asFlow()
            .mapToList(dispatcher)
}
