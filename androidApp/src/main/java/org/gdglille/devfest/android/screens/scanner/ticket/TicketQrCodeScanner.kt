package org.gdglille.devfest.android.screens.scanner.ticket

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import org.gdglille.devfest.android.R
import org.gdglille.devfest.android.components.appbars.TopAppBar
import org.gdglille.devfest.android.screens.scanner.FeatureThatRequiresCameraPermission

@ExperimentalPermissionsApi
@ExperimentalMaterial3Api
@Composable
fun TicketQrCodeScanner(
    modifier: Modifier = Modifier,
    navigateToSettingsScreen: () -> Unit,
    onQrCodeDetected: (String) -> Unit,
    onBackClicked: () -> Unit,
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
