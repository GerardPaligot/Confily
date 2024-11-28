package com.paligot.confily.core.speakers

import com.paligot.confily.core.speakers.entities.SpeakerInfo
import com.paligot.confily.core.speakers.entities.SpeakerItem
import kotlinx.coroutines.flow.Flow

interface SpeakerDao {
    fun fetchSpeaker(eventId: String, speakerId: String): Flow<SpeakerInfo>
    fun fetchSpeakers(eventId: String): Flow<List<SpeakerItem>>
}
