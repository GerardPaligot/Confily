package com.paligot.confily.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.QrCodeScanner
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.action_contacts_scanner
import com.paligot.confily.resources.action_ticket_scanner
import com.paligot.confily.resources.screen_profile
import com.paligot.confily.style.theme.actions.FabAction

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
