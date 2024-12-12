package com.paligot.confily.infos.panes

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun TicketQrCodeScanner(
    navigateToSettingsScreen: () -> Unit,
    onQrCodeDetected: (String) -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier
) {
    Text("Not supported yet for the web experience")
}
