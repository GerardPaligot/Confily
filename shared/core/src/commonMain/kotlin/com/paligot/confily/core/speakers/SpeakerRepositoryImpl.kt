package com.paligot.confily.core.speakers

import com.paligot.confily.core.kvalue.ConferenceSettings
import com.paligot.confily.core.schedules.SessionDao
import com.paligot.confily.core.socials.SocialDao
import com.paligot.confily.core.speakers.entities.Speaker
import com.paligot.confily.core.speakers.entities.SpeakerItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat

class SpeakerRepositoryImpl(
    private val speakerDao: SpeakerDao,
    private val sessionDao: SessionDao,
    private val socialDao: SocialDao,
    private val settings: ConferenceSettings
) : SpeakerRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun speaker(speakerId: String): Flow<Speaker> = settings.fetchEventId()
        .flatMapConcat {
            combine(
                flow = speakerDao.fetchSpeaker(eventId = it, speakerId = speakerId),
                flow2 = sessionDao.fetchSessionsBySpeakerId(eventId = it, speakerId = speakerId),
                flow3 = socialDao.fetchSocials(eventId = it, extId = speakerId),
                transform = { info, sessions, socials ->
                    Speaker(info = info, sessions = sessions, socials = socials)
                }
            )
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun speakers(): Flow<List<SpeakerItem>> = settings.fetchEventId()
        .flatMapConcat { speakerDao.fetchSpeakers(eventId = it) }
}
