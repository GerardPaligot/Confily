package com.paligot.confily.core.socials

import kotlinx.serialization.Serializable

@Serializable
class SocialDb(val type: String, val extId: String, val url: String, val eventId: String)
