package org.gdglille.devfest.android.theme.vitamin.ui.screens.event

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.gdglille.devfest.android.theme.vitamin.ui.components.appbars.TopAppBar
import org.gdglille.devfest.android.theme.vitamin.ui.components.permissions.FeatureThatRequiresCameraPermission
import org.gdglille.devfest.android.ui.camera.TicketCameraPreview
import org.gdglille.devfest.android.ui.resources.R

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
                navigationIcon = {
                    Context(
                        onClick = onBackClicked,
                        contentDescription = stringResource(id = R.string.action_back)
                    )
                }
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
