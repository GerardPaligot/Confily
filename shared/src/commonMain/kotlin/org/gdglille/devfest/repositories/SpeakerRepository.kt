package org.gdglille.devfest.repositories

import com.rickclephas.kmp.nativecoroutines.NativeCoroutineScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import org.gdglille.devfest.database.EventDao
import org.gdglille.devfest.database.SpeakerDao
import org.gdglille.devfest.models.SpeakerItemUi

interface SpeakerRepository {
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
    @NativeCoroutineScope
    private val coroutineScope: CoroutineScope = MainScope()

    override fun speakers(): Flow<ImmutableList<SpeakerItemUi>> = speakerDao.fetchSpeakers(
        eventId = eventDao.fetchEventId()
    )
}
