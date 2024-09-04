package com.paligot.confily.core.repositories

import com.paligot.confily.core.database.EventDao
import com.paligot.confily.core.database.SpeakerDao
import com.paligot.confily.models.ui.SpeakerItemUi
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface SpeakerRepository {
    @NativeCoroutines
    fun speakers(): Flow<ImmutableList<SpeakerItemUi>>

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
    override fun speakers(): Flow<ImmutableList<SpeakerItemUi>> = speakerDao.fetchSpeakers(
        eventId = eventDao.getEventId()
    )
}
