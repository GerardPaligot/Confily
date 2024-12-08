package com.paligot.confily.core.socials

import com.paligot.confily.core.socials.entities.Social
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SocialDaoSettings(
    private val socialQueries: SocialQueries
) : SocialDao {
    override fun fetchSocials(eventId: String, extId: String): Flow<List<Social>> = socialQueries
        .selectSpeakersByEvent(eventId, extId)
        .map { socials -> socials.map(SocialDb::mapToEntity) }
}
