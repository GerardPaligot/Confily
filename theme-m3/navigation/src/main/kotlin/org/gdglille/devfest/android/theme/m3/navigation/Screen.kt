package org.gdglille.devfest.android.theme.m3.navigation

import androidx.annotation.StringRes
import org.gdglille.devfest.android.theme.m3.style.R

sealed class Screen(
    val route: String,
    @StringRes val title: Int
) {
    data object FutureEvents : Screen(route = "events/future", title = R.string.screen_events_future)
    data object PastEvents : Screen(route = "events/past", title = R.string.screen_events_past)
    data object Agenda : Screen(route = "agenda", title = R.string.screen_agenda)
    data object SpeakerList : Screen(route = "speakers", title = R.string.screen_speakers)
    data object MyProfile : Screen(route = "profile", title = R.string.screen_networking)
    data object Contacts : Screen(route = "contacts", title = R.string.screen_networking)
    data object Partners : Screen(route = "partners", title = R.string.screen_partners)
    data object Event : Screen(route = "event", title = R.string.screen_info)
    data object Menus : Screen(route = "menus", title = R.string.screen_info)
    data object QAndA : Screen(route = "qanda", title = R.string.screen_info)
    data object CoC : Screen(route = "coc", title = R.string.screen_info)
}
