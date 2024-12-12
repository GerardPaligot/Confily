package com.paligot.confily.events.routes

import kotlinx.serialization.Serializable

@Serializable
object PastEvents {
    fun navDeeplink() = "c4h://event/events/past"
}
