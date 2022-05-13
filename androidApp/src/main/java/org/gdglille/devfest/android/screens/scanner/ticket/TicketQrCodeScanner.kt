package org.gdglille.devfest.android.screens.scanner.ticket

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.android.R
import org.gdglille.devfest.android.components.appbars.TopAppBar
import org.gdglille.devfest.android.screens.scanner.vcard.FeatureThatRequiresCameraPermission
import org.gdglille.devfest.repositories.AgendaRepository

@Composable
fun TicketQrCodeScannerVm(
    agendaRepository: AgendaRepository,
    modifier: Modifier = Modifier,
    navigateToSettingsScreen: () -> Unit,
    onBackClicked: () -> Unit,
) {
    val viewModel: TicketQrCodeScannerViewModel = viewModel(
        factory = TicketQrCodeScannerViewModel.Factory.create(agendaRepository)
    )
    TicketQrCodeScanner(
        modifier = modifier,
        navigateToSettingsScreen = navigateToSettingsScreen,
        onRefusePermissionClicked = onBackClicked,
        onQrCodeDetected = {
            viewModel.saveTicket(barcode = it.first())
            onBackClicked()
        },
        onBackClicked = onBackClicked
    )
}

@Composable
fun TicketQrCodeScanner(
    modifier: Modifier = Modifier,
    navigateToSettingsScreen: () -> Unit,
    onRefusePermissionClicked: () -> Unit,
    onQrCodeDetected: (List<String>) -> Unit,
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
                    onRefusePermissionClicked = onRefusePermissionClicked,
                    content = {
                        TicketCameraPreview(onQrCodeDetected = onQrCodeDetected)
                    }
                )
            }
        }
    )
}
