package com.paligot.confily.infos.routes

import com.paligot.confily.core.navigation.NavigationBar
import kotlinx.serialization.Serializable

@Serializable
object Info : NavigationBar() {
    fun navDeeplink() = "c4h://event/info"
}
