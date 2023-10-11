package org.gdglille.devfest.android.theme.m3.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.QrCodeScanner
import org.gdglille.devfest.android.theme.m3.style.actions.FabAction
import org.gdglille.devfest.android.theme.m3.style.R

object FabActions {
    val scanTicket = FabAction(
        id = ActionIds.SCAN_TICKET,
        icon = Icons.Outlined.QrCodeScanner,
        contentDescription = R.string.action_ticket_scanner,
        label = R.string.action_ticket_scanner
    )
    val scanContact = FabAction(
        id = ActionIds.SCAN_CONTACTS,
        icon = Icons.Outlined.Add,
        contentDescription = R.string.action_contacts_scanner,
        label = R.string.action_contacts_scanner
    )
    val createProfile = FabAction(
        id = ActionIds.CREATE_PROFILE,
        icon = Icons.Outlined.Add,
        contentDescription = R.string.screen_profile,
        label = R.string.screen_profile
    )
}
