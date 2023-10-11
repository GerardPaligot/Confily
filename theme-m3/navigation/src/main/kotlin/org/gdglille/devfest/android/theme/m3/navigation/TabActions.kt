package org.gdglille.devfest.android.theme.m3.navigation

import org.gdglille.devfest.android.theme.m3.style.actions.TabAction
import org.gdglille.devfest.android.theme.m3.style.R

object TabActions {
    val futureEvents = TabAction(
        route = Screen.FutureEvents.route,
        labelId = R.string.screen_events_future
    )
    val pastEvents = TabAction(
        route = Screen.PastEvents.route,
        labelId = R.string.screen_events_past
    )
    val myProfile = TabAction(
        route = Screen.MyProfile.route,
        labelId = R.string.screen_my_profile
    )
    val contacts = TabAction(
        route = Screen.Contacts.route,
        labelId = R.string.screen_contacts
    )
    val event = TabAction(
        route = Screen.Event.route,
        labelId = R.string.screen_event
    )
    val menus = TabAction(
        route = Screen.Menus.route,
        labelId = R.string.screen_menus
    )
    val qanda = TabAction(
        route = Screen.QAndA.route,
        labelId = R.string.screen_qanda
    )
    val coc = TabAction(
        route = Screen.CoC.route,
        labelId = R.string.screen_coc
    )
}
