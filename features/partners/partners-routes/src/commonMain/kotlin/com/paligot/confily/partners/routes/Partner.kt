package com.paligot.confily.partners.routes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Partner(@SerialName("partnerId") val id: String) {
    companion object {
        fun navDeeplink() = "c4h://event/partners/{partnerId}"
    }
}
