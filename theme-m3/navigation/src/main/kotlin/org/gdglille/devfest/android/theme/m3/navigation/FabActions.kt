package org.gdglille.devfest.android.theme.m3.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.QrCodeScanner
import org.gdglille.devfest.android.theme.m3.style.actions.FabAction
import org.gdglille.devfest.android.shared.resources.Resource
import org.gdglille.devfest.android.shared.resources.action_contacts_scanner
import org.gdglille.devfest.android.shared.resources.action_ticket_scanner
import org.gdglille.devfest.android.shared.resources.screen_profile
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
object FabActions {
    val scanTicket = FabAction(
        id = ActionIds.SCAN_TICKET,
        icon = Icons.Outlined.QrCodeScanner,
        contentDescription = Resource.string.action_ticket_scanner,
        label = Resource.string.action_ticket_scanner
    )
    val scanContact = FabAction(
        id = ActionIds.SCAN_CONTACTS,
        icon = Icons.Outlined.Add,
        contentDescription = Resource.string.action_contacts_scanner,
        label = Resource.string.action_contacts_scanner
    )
    val createProfile = FabAction(
        id = ActionIds.CREATE_PROFILE,
        icon = Icons.Outlined.Add,
        contentDescription = Resource.string.screen_profile,
        label = Resource.string.screen_profile
    )
}
