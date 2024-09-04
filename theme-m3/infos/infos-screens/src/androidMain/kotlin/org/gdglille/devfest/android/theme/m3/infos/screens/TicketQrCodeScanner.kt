package org.gdglille.devfest.android.theme.m3.infos.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.paligot.confily.infos.ui.tickets.TicketCameraPreview
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.screen_ticket_qrcode_scanner
import com.paligot.confily.style.components.permissions.FeatureThatRequiresCameraPermission
import com.paligot.confily.style.theme.appbars.TopAppBar
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketQrCodeScanner(
    navigateToSettingsScreen: () -> Unit,
    onQrCodeDetected: (String) -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = stringResource(Resource.string.screen_ticket_qrcode_scanner),
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
