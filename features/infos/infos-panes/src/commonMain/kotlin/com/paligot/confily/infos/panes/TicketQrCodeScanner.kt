package com.paligot.confily.infos.panes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun TicketQrCodeScanner(
    navigateToSettingsScreen: () -> Unit,
    onQrCodeDetected: (String) -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
)
