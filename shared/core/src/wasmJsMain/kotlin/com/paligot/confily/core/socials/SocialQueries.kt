package com.paligot.confily.core.socials

import com.paligot.confily.core.combineAllSerializableScopedFlow
import com.paligot.confily.core.putSerializableScoped
import com.paligot.confily.core.socials.SocialQueries.Scopes.SOCIALS
import com.russhwolf.settings.ObservableSettings
import kotlinx.coroutines.flow.Flow

class SocialQueries(private val settings: ObservableSettings) {
    private object Scopes {
        const val SOCIALS = "socials"
    }

    fun upsertSocial(social: SocialDb) {
        val key = "${social.url}:${social.type}:${social.extId}"
        settings.putSerializableScoped(SOCIALS, key, social)
    }

    fun selectSpeakersByEvent(eventId: String, extId: String): Flow<List<SocialDb>> =
        settings.combineAllSerializableScopedFlow(SOCIALS) {
            it.eventId == eventId && it.extId == extId
        }
}
