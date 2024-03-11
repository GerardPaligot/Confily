package org.gdglille.devfest.android.theme.m3.navigation

import org.gdglille.devfest.android.theme.m3.style.actions.TabAction
import org.gdglille.devfest.android.shared.resources.Resource
import org.gdglille.devfest.android.shared.resources.screen_coc
import org.gdglille.devfest.android.shared.resources.screen_contacts
import org.gdglille.devfest.android.shared.resources.screen_event
import org.gdglille.devfest.android.shared.resources.screen_events_future
import org.gdglille.devfest.android.shared.resources.screen_events_past
import org.gdglille.devfest.android.shared.resources.screen_menus
import org.gdglille.devfest.android.shared.resources.screen_my_profile
import org.gdglille.devfest.android.shared.resources.screen_qanda
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
object TabActions {
    val futureEvents = TabAction(
        route = Screen.FutureEvents.route,
        labelId = Resource.string.screen_events_future
    )
    val pastEvents = TabAction(
        route = Screen.PastEvents.route,
        labelId = Resource.string.screen_events_past
    )
    val myProfile = TabAction(
        route = Screen.MyProfile.route,
        labelId = Resource.string.screen_my_profile
    )
    val contacts = TabAction(
        route = Screen.Contacts.route,
        labelId = Resource.string.screen_contacts
    )
    val event = TabAction(
        route = Screen.Event.route,
        labelId = Resource.string.screen_event
    )
    val menus = TabAction(
        route = Screen.Menus.route,
        labelId = Resource.string.screen_menus
    )
    val qanda = TabAction(
        route = Screen.QAndA.route,
        labelId = Resource.string.screen_qanda
    )
    val coc = TabAction(
        route = Screen.CoC.route,
        labelId = Resource.string.screen_coc
    )
}
