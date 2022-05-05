package org.gdglille.devfest.android.screens

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Group
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
        imageVectorOutlined = Icons.Outlined.Event
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
                id = ActionItemId.QrCodeScannerActionItem
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
                icon = Icons.Default.Report,
                contentDescription = R.string.action_report,
                id = ActionItemId.ReportActionItem
            )
        )
    )
}
