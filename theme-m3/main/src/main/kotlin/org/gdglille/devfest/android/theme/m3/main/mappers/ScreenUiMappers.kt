package org.gdglille.devfest.android.theme.m3.main.mappers

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.gdglille.devfest.android.theme.m3.navigation.BottomActions
import org.gdglille.devfest.android.theme.m3.navigation.FabActions
import org.gdglille.devfest.android.theme.m3.navigation.Screen
import org.gdglille.devfest.android.theme.m3.navigation.TabActions
import org.gdglille.devfest.android.theme.m3.navigation.TopActions
import org.gdglille.devfest.android.theme.m3.style.actions.BottomAction
import org.gdglille.devfest.android.theme.m3.style.actions.BottomActionsUi
import org.gdglille.devfest.android.theme.m3.style.actions.FabAction
import org.gdglille.devfest.android.theme.m3.style.actions.ScreenUi
import org.gdglille.devfest.android.theme.m3.style.actions.TabAction
import org.gdglille.devfest.android.theme.m3.style.actions.TabActionsUi
import org.gdglille.devfest.android.theme.m3.style.actions.TopActionsUi
import org.gdglille.devfest.models.ui.ScaffoldConfigUi

fun convertToModelUi(
    route: String,
    innerRoute: String?,
    config: ScaffoldConfigUi,
    hasFilters: Boolean
) = ScreenUi(
    title = route.title(),
    topActionsUi = route.topActions(config, hasFilters),
    tabActionsUi = route.tabActions(config),
    fabAction = innerRoute?.fabAction(route, config),
    bottomActionsUi = bottomActions(config)
)

private fun String.title(): Int = when (this) {
    Screen.Agenda.route -> Screen.Agenda.title
    Screen.SpeakerList.route -> Screen.SpeakerList.title
    Screen.MyProfile.route -> Screen.MyProfile.title
    Screen.Contacts.route -> Screen.Contacts.title
    Screen.Partners.route -> Screen.Partners.title
    Screen.Event.route -> Screen.Event.title
    Screen.Menus.route -> Screen.Menus.title
    Screen.QAndA.route -> Screen.QAndA.title
    Screen.CoC.route -> Screen.CoC.title
    else -> TODO()
}

private fun String.topActions(config: ScaffoldConfigUi, hasFilters: Boolean): TopActionsUi =
    when (this) {
        Screen.Agenda.route -> TopActionsUi(
            actions = persistentListOf(if (hasFilters) TopActions.filtersFilled else TopActions.filters)
        )

        Screen.Contacts.route -> TopActionsUi(
            actions = if (config.hasUsersInNetworking) persistentListOf(TopActions.export) else persistentListOf()
        )

        Screen.Event.route -> TopActionsUi(
            actions = persistentListOf(TopActions.disconnect),
            maxActions = 0
        )

        else -> TopActionsUi()
    }

private fun String.tabActions(config: ScaffoldConfigUi): TabActionsUi = when (this) {
    Screen.Agenda.route -> TabActionsUi(
        scrollable = true,
        actions = config.agendaTabs
            .map {
                val label = DateTimeFormatter.ofPattern("dd MMM").format(LocalDate.parse(it))
                TabAction(route = it, 0, label)
            }
            .toImmutableList()
    )

    Screen.MyProfile.route, Screen.Contacts.route -> TabActionsUi(
        actions = if (config.hasProfile) persistentListOf(TabActions.myProfile, TabActions.contacts)
        else persistentListOf(TabActions.myProfile)
    )

    Screen.Event.route, Screen.Menus.route, Screen.QAndA.route, Screen.CoC.route -> TabActionsUi(
        scrollable = true,
        actions = arrayListOf<TabAction>().apply {
            add(TabActions.event)
            if (config.hasMenus) {
                add(TabActions.menus)
            }
            if (config.hasQAndA) {
                add(TabActions.qanda)
            }
            add(TabActions.coc)
        }.toImmutableList()
    )

    else -> TabActionsUi()
}

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
