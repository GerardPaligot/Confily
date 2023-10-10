package org.gdglille.devfest.android.theme.m3.navigation

import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.android.ui.resources.actions.BottomAction

object BottomActions {
    val agenda = BottomAction(
        route = Screen.Agenda.route,
        icon = R.drawable.ic_mtrl_event_line,
        iconSelected = R.drawable.ic_mtrl_event_fill,
        label = R.string.screen_agenda,
        contentDescription = null
    )
    val speakers = BottomAction(
        route = Screen.SpeakerList.route,
        icon = R.drawable.ic_mtrl_interpreter_mode_line,
        iconSelected = R.drawable.ic_mtrl_interpreter_mode_fill,
        label = R.string.screen_speakers,
        contentDescription = null,
    )
    val networking = BottomAction(
        route = Screen.Networking.route,
        icon = R.drawable.ic_mtrl_hub_line,
        iconSelected = R.drawable.ic_mtrl_hub_fill,
        label = R.string.screen_networking,
        contentDescription = null,
        selectedRoutes = listOf(Screen.MyProfile.route, Screen.Contacts.route)
    )
    val partners = BottomAction(
        route = Screen.Partners.route,
        icon = R.drawable.ic_mtrl_handshake_line,
        iconSelected = R.drawable.ic_mtrl_handshake_fill,
        label = R.string.screen_partners,
        contentDescription = null,
    )
    val info = BottomAction(
        route = Screen.Info.route,
        label = R.string.screen_info,
        icon = R.drawable.ic_mtrl_info_line,
        iconSelected = R.drawable.ic_mtrl_info_fill,
        contentDescription = null,
        selectedRoutes = listOf(
            Screen.Event.route,
            Screen.Menus.route,
            Screen.QAndA.route,
            Screen.CoC.route
        )
    )
}
