package com.paligot.confily.navigation

import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.screen_coc
import com.paligot.confily.resources.screen_contacts
import com.paligot.confily.resources.screen_event
import com.paligot.confily.resources.screen_events_future
import com.paligot.confily.resources.screen_events_past
import com.paligot.confily.resources.screen_menus
import com.paligot.confily.resources.screen_my_profile
import com.paligot.confily.resources.screen_partners
import com.paligot.confily.resources.screen_partners_activities
import com.paligot.confily.resources.screen_qanda
import com.paligot.confily.style.theme.actions.TabAction

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
    val partners = TabAction(
        route = Screen.Partner.route,
        labelId = Resource.string.screen_partners
    )
    val activities = TabAction(
        route = Screen.PartnerActivities.route,
        labelId = Resource.string.screen_partners_activities
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
