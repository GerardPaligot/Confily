package org.gdglille.devfest.android.theme.m3.main.mappers

import kotlinx.collections.immutable.toImmutableList
import org.gdglille.devfest.android.theme.m3.navigation.BottomActions
import org.gdglille.devfest.android.theme.m3.navigation.FabActions
import org.gdglille.devfest.android.theme.m3.navigation.Screen
import org.gdglille.devfest.android.theme.m3.style.actions.BottomAction
import org.gdglille.devfest.android.theme.m3.style.actions.BottomActionsUi
import org.gdglille.devfest.android.theme.m3.style.actions.FabAction
import org.gdglille.devfest.android.theme.m3.style.actions.ScreenUi
import org.gdglille.devfest.models.ui.ScaffoldConfigUi

fun convertToModelUi(
    route: String,
    innerRoute: String?,
    config: ScaffoldConfigUi
) = ScreenUi(
    fabAction = innerRoute?.fabAction(route, config),
    bottomActionsUi = bottomActions(config)
)

private fun String.fabAction(route: String, config: ScaffoldConfigUi): FabAction? {
    if (route == Screen.Agenda.route || route == Screen.SpeakerList.route || route == Screen.Partners.route) return null
    return when (this) {
        Screen.MyProfile.route -> if (!config.hasProfile) FabActions.createProfile else null
        Screen.Contacts.route -> FabActions.scanContact
        Screen.Event.route -> if (config.hasBilletWebTicket) FabActions.scanTicket else null
        else -> null
    }
}

private fun bottomActions(config: ScaffoldConfigUi): BottomActionsUi = BottomActionsUi(
    actions = arrayListOf<BottomAction>().apply {
        add(BottomActions.agenda)
        add(BottomActions.speakers)
        if (config.hasNetworking) {
            add(BottomActions.myProfile)
        }
        if (config.hasPartnerList) {
            add(BottomActions.partners)
        }
        add(BottomActions.event)
    }.toImmutableList()
)
