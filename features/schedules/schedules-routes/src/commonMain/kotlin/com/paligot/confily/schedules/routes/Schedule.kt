package com.paligot.confily.schedules.routes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Schedule(@SerialName("scheduleId") val id: String) {
    fun deeplink() = "c4h://event/schedules/$id"

    companion object {
        fun navDeeplink() = "c4h://event/schedules/{scheduleId}"
    }
}
