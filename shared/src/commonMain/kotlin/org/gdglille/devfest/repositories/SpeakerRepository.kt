package org.gdglille.devfest.repositories

import kotlinx.coroutines.flow.Flow
import org.gdglille.devfest.database.SpeakerDao
import org.gdglille.devfest.models.SpeakerItemUi

interface SpeakerRepository {
    suspend fun speakers(): Flow<List<SpeakerItemUi>>

    object Factory {
        fun create(speakerDao: SpeakerDao): SpeakerRepository = SpeakerRepositoryImpl(speakerDao)
    }
}

class SpeakerRepositoryImpl(
    private val speakerDao: SpeakerDao
) : SpeakerRepository {
    override suspend fun speakers(): Flow<List<SpeakerItemUi>> = speakerDao.fetchSpeakers()
}
