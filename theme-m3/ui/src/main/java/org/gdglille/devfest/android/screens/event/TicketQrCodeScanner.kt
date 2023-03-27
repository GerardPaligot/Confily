package org.gdglille.devfest.android.screens.event

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.gdglille.devfest.android.components.appbars.TopAppBar
import org.gdglille.devfest.android.components.permissions.FeatureThatRequiresCameraPermission
import org.gdglille.devfest.android.ui.camera.TicketCameraPreview
import org.gdglille.devfest.android.ui.resources.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketQrCodeScanner(
    navigateToSettingsScreen: () -> Unit,
    onQrCodeDetected: (String) -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.screen_ticket_qrcode_scanner),
                navigationIcon = { Back(onClick = onBackClicked) }
            )
        },
        content = {
            Box(modifier = Modifier.padding(it)) {
                FeatureThatRequiresCameraPermission(
                    navigateToSettingsScreen = navigateToSettingsScreen,
                    onRefusePermissionClicked = onBackClicked,
                    content = {
                        TicketCameraPreview(onQrCodeDetected = { barcode ->
                            onQrCodeDetected(barcode.first())
                        })
                    }
                )
            }
        }
    )
}
