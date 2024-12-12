package com.paligot.confily.events.routes

import kotlinx.serialization.Serializable

@Serializable
object FutureEvents {
    fun navDeeplink() = "c4h://event/events/future"
}
