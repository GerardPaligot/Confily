package org.gdglille.devfest.android.theme.m3.navigation

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
import org.gdglille.devfest.android.shared.resources.Resource
import org.gdglille.devfest.android.theme.m3.style.actions.NavigationAction
import org.gdglille.devfest.android.shared.resources.screen_agenda
import org.gdglille.devfest.android.shared.resources.screen_info
import org.gdglille.devfest.android.shared.resources.screen_networking
import org.gdglille.devfest.android.shared.resources.screen_partners
import org.gdglille.devfest.android.shared.resources.screen_speakers
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
object BottomActions {
    val agenda = NavigationAction(
        route = Screen.ScheduleList.route,
        icon = Icons.Outlined.Event,
        iconSelected = Icons.Filled.Event,
        label = Resource.string.screen_agenda
    )
    val speakers = NavigationAction(
        route = Screen.SpeakerList.route,
        icon = Icons.Outlined.InterpreterMode,
        iconSelected = Icons.Filled.InterpreterMode,
        label = Resource.string.screen_speakers
    )
    val myProfile = NavigationAction(
        route = Screen.MyProfile.route,
        icon = Icons.Outlined.Hub,
        iconSelected = Icons.Filled.Hub,
        label = Resource.string.screen_networking
    )
    val partners = NavigationAction(
        route = Screen.PartnerList.route,
        icon = Icons.Outlined.Handshake,
        iconSelected = Icons.Filled.Handshake,
        label = Resource.string.screen_partners
    )
    val event = NavigationAction(
        route = Screen.Event.route,
        icon = Icons.Outlined.Info,
        iconSelected = Icons.Filled.Info,
        label = Resource.string.screen_info
    )
}
