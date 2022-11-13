package org.gdglille.devfest.repositories

import com.rickclephas.kmp.nativecoroutines.NativeCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import org.gdglille.devfest.database.SpeakerDao
import org.gdglille.devfest.models.SpeakerItemUi

interface SpeakerRepository {
    fun speakers(): Flow<List<SpeakerItemUi>>

    object Factory {
        fun create(speakerDao: SpeakerDao): SpeakerRepository = SpeakerRepositoryImpl(speakerDao)
    }
}

class SpeakerRepositoryImpl(
    private val speakerDao: SpeakerDao
) : SpeakerRepository {
    @NativeCoroutineScope
    private val coroutineScope: CoroutineScope = MainScope()

    override fun speakers(): Flow<List<SpeakerItemUi>> = speakerDao.fetchSpeakers()
}
