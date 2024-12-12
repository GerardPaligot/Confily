package com.paligot.confily.networking.routes

import kotlinx.serialization.Serializable

@Serializable
object NewProfile {
    fun navDeeplink() = "c4h://event/profile/new"
}
