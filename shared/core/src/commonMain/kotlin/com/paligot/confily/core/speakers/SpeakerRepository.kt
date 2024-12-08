package com.paligot.confily.core.speakers

import com.paligot.confily.core.kvalue.ConferenceSettings
import com.paligot.confily.core.schedules.SessionDao
import com.paligot.confily.core.socials.SocialDao
import com.paligot.confily.core.speakers.entities.Speaker
import com.paligot.confily.core.speakers.entities.SpeakerItem
import kotlinx.coroutines.flow.Flow

interface SpeakerRepository {
    fun speaker(speakerId: String): Flow<Speaker>
    fun speakers(): Flow<List<SpeakerItem>>

    object Factory {
        fun create(
            speakerDao: SpeakerDao,
            sessionDao: SessionDao,
            socialDao: SocialDao,
            settings: ConferenceSettings
        ): SpeakerRepository = SpeakerRepositoryImpl(speakerDao, sessionDao, socialDao, settings)
    }
}
