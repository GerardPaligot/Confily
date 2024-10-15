package com.paligot.confily.core.speakers

import com.paligot.confily.models.ui.SpeakerItemUi
import com.paligot.confily.models.ui.SpeakerUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface SpeakerDao {
    fun fetchSpeaker(eventId: String, speakerId: String): Flow<SpeakerUi>
    fun fetchSpeakers(eventId: String): Flow<ImmutableList<SpeakerItemUi>>
}
