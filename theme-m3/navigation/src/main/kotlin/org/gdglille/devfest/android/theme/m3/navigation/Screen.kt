package org.gdglille.devfest.android.theme.m3.navigation

import androidx.annotation.StringRes
import org.gdglille.devfest.android.ui.resources.R

sealed class Screen(
    val route: String,
    @StringRes val title: Int
) {
    object FutureEvents : Screen(route = "events/future", title = R.string.screen_events_future)
    object PastEvents : Screen(route = "events/past", title = R.string.screen_events_past)
    object Agenda : Screen(route = "agenda", title = R.string.screen_agenda)
    object SpeakerList : Screen(route = "speakers", title = R.string.screen_speakers)
    object Networking : Screen(route = "networking", title = R.string.screen_networking)
    object MyProfile : Screen(route = "profile", title = R.string.screen_networking)
    object Contacts : Screen(route = "contacts", title = R.string.screen_networking)
    object Partners : Screen(route = "partners", title = R.string.screen_partners)
    object Info : Screen(route = "info", title = R.string.screen_info)
    object Event : Screen(route = "event", title = R.string.screen_info)
    object Menus : Screen(route = "menus", title = R.string.screen_info)
    object QAndA : Screen(route = "qanda", title = R.string.screen_info)
    object CoC : Screen(route = "coc", title = R.string.screen_info)
}
