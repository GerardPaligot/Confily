package org.gdglille.devfest.android.screens

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Handshake
import androidx.compose.material.icons.outlined.LocalActivity
import androidx.compose.ui.graphics.vector.ImageVector
import org.gdglille.devfest.android.components.appbars.ActionItem
import org.gdglille.devfest.android.components.appbars.ActionItemId
import org.gdglille.devfest.android.ui.R

sealed class Screen(
    val route: String,
    @StringRes val title: Int,
    val imageVectorFilled: ImageVector,
    val imageVectorOutlined: ImageVector,
    val actions: List<ActionItem> = emptyList()
) {
    fun imageVector(selected: Boolean) = if (selected) imageVectorFilled else imageVectorOutlined

    object Agenda : Screen(
        route = "agenda",
        title = R.string.screen_agenda,
        imageVectorFilled = Icons.Filled.Event,
        imageVectorOutlined = Icons.Outlined.Event,
        actions = arrayListOf(
            ActionItem(
                icon = Icons.Filled.Star,
                contentDescription = R.string.action_filtering_favorites,
                id = ActionItemId.FavoriteSchedulesActionItem
            )
        )
    )

    object Networking : Screen(
        route = "networking",
        title = R.string.screen_networking,
        imageVectorFilled = Icons.Filled.Group,
        imageVectorOutlined = Icons.Outlined.Group,
        actions = arrayListOf(
            ActionItem(
                icon = Icons.Filled.QrCodeScanner,
                contentDescription = R.string.action_qrcode_scanner,
                id = ActionItemId.VCardQrCodeScannerActionItem
            ),
            ActionItem(
                icon = Icons.Filled.QrCode,
                contentDescription = R.string.action_qrcode_generator,
                id = ActionItemId.QrCodeActionItem
            )
        )
    )

    object Event : Screen(
        route = "event",
        title = R.string.screen_event,
        imageVectorFilled = Icons.Filled.LocalActivity,
        imageVectorOutlined = Icons.Outlined.LocalActivity,
        actions = arrayListOf(
            ActionItem(
                icon = Icons.Filled.ConfirmationNumber,
                contentDescription = R.string.action_ticket_qrcode_scanner,
                id = ActionItemId.TicketQrCodeScannerActionItem
            ),
            ActionItem(
                icon = Icons.Default.Report,
                contentDescription = R.string.action_report,
                id = ActionItemId.ReportActionItem
            )
        )
    )

    object Partners : Screen(
        route = "partners",
        title = R.string.screen_partners,
        imageVectorFilled = Icons.Filled.Handshake,
        imageVectorOutlined = Icons.Outlined.Handshake,
    )
}
