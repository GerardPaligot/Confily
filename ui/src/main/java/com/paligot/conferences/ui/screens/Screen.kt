package com.paligot.conferences.ui.screens

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
import com.paligot.conferences.ui.components.appbars.ActionItem
import com.paligot.conferences.ui.components.appbars.ActionItemId

sealed class Screen(
    val route: String,
    val title: String,
    val imageVectorFilled: ImageVector,
    val imageVectorOutlined: ImageVector,
    val actions: List<ActionItem> = emptyList()
) {
    fun imageVector(selected: Boolean) = if (selected) imageVectorFilled else imageVectorOutlined

    object Agenda : Screen(
        route = "agenda",
        title = "Agenda",
        imageVectorFilled = Icons.Filled.Event,
        imageVectorOutlined = Icons.Outlined.Event
    )

    object Networking : Screen(
        route = "networking",
        title = "Networking",
        imageVectorFilled = Icons.Filled.Group,
        imageVectorOutlined = Icons.Outlined.Group,
        actions = arrayListOf(
            ActionItem(
                icon = Icons.Filled.QrCodeScanner,
                contentDescription = "QrCode scanner",
                id = ActionItemId.QrCodeScannerActionItem
            ),
            ActionItem(
                icon = Icons.Filled.QrCode,
                contentDescription = "QrCode Generator",
                id = ActionItemId.QrCodeActionItem
            )
        )
    )

    object Event : Screen(
        route = "event",
        title = "Event Info",
        imageVectorFilled = Icons.Filled.LocalActivity,
        imageVectorOutlined = Icons.Outlined.LocalActivity,
        actions = arrayListOf(
            ActionItem(
                icon = Icons.Default.Report,
                contentDescription = "Report something to organizers",
                id = ActionItemId.ReportActionItem
            )
        )
    )
}
