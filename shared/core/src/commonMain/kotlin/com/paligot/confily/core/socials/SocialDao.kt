package com.paligot.confily.core.socials

import com.paligot.confily.core.socials.entities.Social
import kotlinx.coroutines.flow.Flow

interface SocialDao {
    fun fetchSocials(eventId: String, extId: String): Flow<List<Social>>
}
