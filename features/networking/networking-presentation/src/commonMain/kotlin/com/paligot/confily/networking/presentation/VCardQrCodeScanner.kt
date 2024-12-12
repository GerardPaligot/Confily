package com.paligot.confily.networking.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.paligot.confily.models.ui.VCardModel

@Composable
expect fun VCardQrCodeScanner(
    navigateToSettingsScreen: () -> Unit,
    onQrCodeDetected: (VCardModel) -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
)
