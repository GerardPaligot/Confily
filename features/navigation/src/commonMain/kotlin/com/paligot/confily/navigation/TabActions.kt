package com.paligot.confily.navigation

import com.paligot.confily.events.routes.FutureEvents
import com.paligot.confily.events.routes.PastEvents
import com.paligot.confily.infos.routes.CoC
import com.paligot.confily.infos.routes.Info
import com.paligot.confily.infos.routes.MapList
import com.paligot.confily.infos.routes.Menus
import com.paligot.confily.infos.routes.QAndA
import com.paligot.confily.infos.routes.TeamMembers
import com.paligot.confily.networking.routes.Contacts
import com.paligot.confily.networking.routes.MyProfile
import com.paligot.confily.partners.routes.Partner
import com.paligot.confily.partners.routes.PartnerActivities
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.screen_coc
import com.paligot.confily.resources.screen_contacts
import com.paligot.confily.resources.screen_event
import com.paligot.confily.resources.screen_events_future
import com.paligot.confily.resources.screen_events_past
import com.paligot.confily.resources.screen_maps
import com.paligot.confily.resources.screen_menus
import com.paligot.confily.resources.screen_my_profile
import com.paligot.confily.resources.screen_partners
import com.paligot.confily.resources.screen_partners_activities
import com.paligot.confily.resources.screen_qanda
import com.paligot.confily.resources.screen_team
import com.paligot.confily.style.theme.actions.TabAction

object TabActions {
    val futureEvents = TabAction(
        route = FutureEvents.navDeeplink(),
        labelId = Resource.string.screen_events_future
    )
    val pastEvents = TabAction(
        route = PastEvents.navDeeplink(),
        labelId = Resource.string.screen_events_past
    )
    val myProfile = TabAction(
        route = MyProfile.navDeeplink(),
        labelId = Resource.string.screen_my_profile
    )
    val contacts = TabAction(
        route = Contacts.navDeeplink(),
        labelId = Resource.string.screen_contacts
    )
    val partners = TabAction(
        route = Partner.navDeeplink(),
        labelId = Resource.string.screen_partners
    )
    val activities = TabAction(
        route = PartnerActivities.navDeeplink(),
        labelId = Resource.string.screen_partners_activities
    )
    val event = TabAction(
        route = Info.navDeeplink(),
        labelId = Resource.string.screen_event
    )
    val maps = TabAction(
        route = MapList.navDeeplink(),
        labelId = Resource.string.screen_maps
    )
    val menus = TabAction(
        route = Menus.navDeeplink(),
        labelId = Resource.string.screen_menus
    )
    val qanda = TabAction(
        route = QAndA.navDeeplink(),
        labelId = Resource.string.screen_qanda
    )
    val coc = TabAction(
        route = CoC.navDeeplink(),
        labelId = Resource.string.screen_coc
    )
    val teamMembers = TabAction(
        route = TeamMembers.navDeeplink(),
        labelId = Resource.string.screen_team
    )
}
