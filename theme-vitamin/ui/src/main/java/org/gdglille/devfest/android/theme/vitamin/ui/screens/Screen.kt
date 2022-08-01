package org.gdglille.devfest.android.theme.vitamin.ui.screens

import androidx.annotation.StringRes
import org.gdglille.devfest.android.theme.vitamin.ui.R
import org.gdglille.devfest.android.ui.resources.BottomAction
import org.gdglille.devfest.android.ui.resources.TabAction
import org.gdglille.devfest.android.ui.resources.TopAction

object TopActionIds {
    const val SHARE_ID = 0
}

object TopActions {
    val share = TopAction(
        id = TopActionIds.SHARE_ID,
        icon = R.drawable.ic_vtmn_share_line,
        contentDescription = R.string.action_share_talk
    )
}

object TabActions {
    val myProfile = TabAction(route = Screen.MyProfile.route, label = R.string.screen_my_profile)
    val contacts = TabAction(route = Screen.Contacts.route, label = R.string.screen_contacts)
    val event = TabAction(route = Screen.Event.route, label = R.string.screen_event)
    val menus = TabAction(route = Screen.Menus.route, label = R.string.screen_menus)
    val qanda = TabAction(route = Screen.QAndA.route, label = R.string.screen_qanda)
    val coc = TabAction(route = Screen.CoC.route, label = R.string.screen_coc)
}

object BottomActions {
    val agenda = BottomAction(
        route = Screen.Agenda.route,
        icon = R.drawable.ic_vtmn_calendar_line,
        iconSelected = R.drawable.ic_vtmn_calendar_fill,
        label = R.string.screen_agenda,
        contentDescription = null
    )
    val speakers = BottomAction(
        route = Screen.SpeakerList.route,
        icon = R.drawable.ic_vtmn_mic_vtmn_line,
        iconSelected = R.drawable.ic_vtmn_mic_vtmn_fill,
        label = R.string.screen_speakers,
        contentDescription = null,
    )
    val networking = BottomAction(
        route = Screen.MyProfile.route,
        icon = R.drawable.ic_vtmn_mic_vtmn_line,
        iconSelected = R.drawable.ic_vtmn_mic_vtmn_fill,
        label = R.string.screen_networking,
        contentDescription = null,
        selectedRoutes = listOf(Screen.MyProfile.route, Screen.Contacts.route)
    )
    val partners = BottomAction(
        route = Screen.Partners.route,
        icon = R.drawable.ic_vtmn_earth_line,
        iconSelected = R.drawable.ic_vtmn_earth_fill,
        label = R.string.screen_partners,
        contentDescription = null,
    )
    val info = BottomAction(
        route = Screen.Event.route,
        label = R.string.screen_info,
        icon = R.drawable.ic_vtmn_information_line,
        iconSelected = R.drawable.ic_vtmn_information_fill,
        contentDescription = null,
        selectedRoutes = listOf(Screen.Event.route, Screen.Menus.route, Screen.QAndA.route, Screen.CoC.route)
    )
}

sealed class Screen(
    val route: String,
    @StringRes val title: Int
) {
    object Agenda : Screen(route = "agenda", title = R.string.screen_agenda)
    object SpeakerList : Screen(route = "speakers", title = R.string.screen_speakers)
    object MyProfile : Screen(route = "profile", title = R.string.screen_my_profile)
    object Contacts : Screen(route = "contacts", title = R.string.screen_contacts)
    object Partners : Screen(route = "partners", title = R.string.screen_partners)
    object Event : Screen(route = "event", title = R.string.screen_info)
    object Menus : Screen(route = "menus", title = R.string.screen_info)
    object QAndA : Screen(route = "qanda", title = R.string.screen_info)
    object CoC : Screen(route = "coc", title = R.string.screen_info)
}
