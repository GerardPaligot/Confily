package com.paligot.confily.networking.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.paligot.confily.models.ui.VCardModel

@Composable
actual fun VCardQrCodeScanner(
    navigateToSettingsScreen: () -> Unit,
    onQrCodeDetected: (VCardModel) -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier
) {
    Text("Not supported yet for the web experience")
}
