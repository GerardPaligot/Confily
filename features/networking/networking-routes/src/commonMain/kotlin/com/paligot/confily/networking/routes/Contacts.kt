package com.paligot.confily.networking.routes

import kotlinx.serialization.Serializable

@Serializable
object Contacts {
    fun navDeeplink() = "c4h://event/contacts"
}
