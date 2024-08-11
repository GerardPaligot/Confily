package org.gdglille.devfest.repositories

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import org.gdglille.devfest.database.EventDao
import org.gdglille.devfest.database.SpeakerDao
import org.gdglille.devfest.models.ui.SpeakerItemUi

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
