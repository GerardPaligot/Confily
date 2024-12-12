package com.paligot.confily.networking.routes

import com.paligot.confily.core.navigation.NavigationBar
import kotlinx.serialization.Serializable

@Serializable
object MyProfile : NavigationBar() {
    fun navDeeplink() = "c4h://event/profile/me"
}
