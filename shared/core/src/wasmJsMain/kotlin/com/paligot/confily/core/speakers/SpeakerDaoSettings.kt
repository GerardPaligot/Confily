package com.paligot.confily.core.speakers

import com.paligot.confily.core.schedules.mapToEntity
import com.paligot.confily.core.schedules.mapToInfoEntity
import com.paligot.confily.core.speakers.entities.SpeakerInfo
import com.paligot.confily.core.speakers.entities.SpeakerItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SpeakerDaoSettings(
    private val speakerQueries: SpeakerQueries
) : SpeakerDao {
    override fun fetchSpeaker(eventId: String, speakerId: String): Flow<SpeakerInfo> =
        speakerQueries.selectSpeaker(speakerId)
            .map { it.mapToInfoEntity() }

    override fun fetchSpeakers(eventId: String): Flow<List<SpeakerItem>> =
        speakerQueries.selectSpeakersByEvent(eventId)
            .map { speakers ->
                speakers.map { speaker -> speaker.mapToEntity() }
            }
}
