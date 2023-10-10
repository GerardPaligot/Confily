package org.gdglille.devfest.android.theme.m3.navigation

import org.gdglille.devfest.android.ui.resources.actions.FabAction
import org.gdglille.devfest.android.ui.resources.R

object FabActions {
    val scanTicket = FabAction(
        id = ActionIds.SCAN_TICKET,
        icon = R.drawable.ic_mtrl_qr_code_scanner_line,
        contentDescription = R.string.action_ticket_scanner,
        label = R.string.action_ticket_scanner
    )
    val scanContact = FabAction(
        id = ActionIds.SCAN_CONTACTS,
        icon = R.drawable.ic_mtrl_add_line,
        contentDescription = R.string.action_contacts_scanner,
        label = R.string.action_contacts_scanner
    )
    val createProfile = FabAction(
        id = ActionIds.CREATE_PROFILE,
        icon = R.drawable.ic_mtrl_add_line,
        contentDescription = R.string.screen_profile,
        label = R.string.screen_profile
    )
}
