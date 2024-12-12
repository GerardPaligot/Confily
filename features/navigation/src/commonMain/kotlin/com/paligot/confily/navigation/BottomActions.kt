package com.paligot.confily.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.InterpreterMode
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Handshake
import androidx.compose.material.icons.outlined.Hub
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.InterpreterMode
import com.paligot.confily.core.navigation.NavigationAction
import com.paligot.confily.infos.routes.Info
import com.paligot.confily.networking.routes.MyProfile
import com.paligot.confily.partners.routes.PartnerList
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.screen_agenda
import com.paligot.confily.resources.screen_info
import com.paligot.confily.resources.screen_networking
import com.paligot.confily.resources.screen_partners
import com.paligot.confily.resources.screen_speakers
import com.paligot.confily.schedules.routes.ScheduleList
import com.paligot.confily.speakers.routes.SpeakerList

object BottomActions {
    val agenda = NavigationAction(
        route = ScheduleList,
        icon = Icons.Outlined.Event,
        iconSelected = Icons.Filled.Event,
        label = Resource.string.screen_agenda
    )
    val speakers = NavigationAction(
        route = SpeakerList,
        icon = Icons.Outlined.InterpreterMode,
        iconSelected = Icons.Filled.InterpreterMode,
        label = Resource.string.screen_speakers
    )
    val myProfile = NavigationAction(
        route = MyProfile,
        icon = Icons.Outlined.Hub,
        iconSelected = Icons.Filled.Hub,
        label = Resource.string.screen_networking
    )
    val partners = NavigationAction(
        route = PartnerList,
        icon = Icons.Outlined.Handshake,
        iconSelected = Icons.Filled.Handshake,
        label = Resource.string.screen_partners
    )
    val event = NavigationAction(
        route = Info,
        icon = Icons.Outlined.Info,
        iconSelected = Icons.Filled.Info,
        label = Resource.string.screen_info
    )
}
