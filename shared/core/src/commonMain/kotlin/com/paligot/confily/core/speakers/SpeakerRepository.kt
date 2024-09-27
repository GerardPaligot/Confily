package com.paligot.confily.core.speakers

import com.paligot.confily.core.events.EventDao
import com.paligot.confily.models.ui.SpeakerItemUi
import com.paligot.confily.models.ui.SpeakerUi
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat

interface SpeakerRepository {
    @NativeCoroutines
    fun speakers(): Flow<ImmutableList<SpeakerItemUi>>

    @NativeCoroutines
    fun speaker(speakerId: String): Flow<SpeakerUi>

    object Factory {
        fun create(
            speakerDao: SpeakerDao,
            eventDao: EventDao
        ): SpeakerRepository = SpeakerRepositoryImpl(speakerDao, eventDao)
    }
}

class SpeakerRepositoryImpl(
    private val speakerDao: SpeakerDao,
    private val eventDao: EventDao
) : SpeakerRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun speaker(speakerId: String): Flow<SpeakerUi> = eventDao.fetchEventId()
        .flatMapConcat { speakerDao.fetchSpeaker(eventId = it, speakerId = speakerId) }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun speakers(): Flow<ImmutableList<SpeakerItemUi>> = eventDao.fetchEventId()
        .flatMapConcat { speakerDao.fetchSpeakers(eventId = it) }
}
