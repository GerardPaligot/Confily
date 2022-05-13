package org.gdglille.devfest.android.screens.scanner.vcard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.android.R
import org.gdglille.devfest.android.components.appbars.TopAppBar
import org.gdglille.devfest.android.screens.scanner.FeatureThatRequiresCameraPermission
import org.gdglille.devfest.models.UserNetworkingUi
import org.gdglille.devfest.repositories.UserRepository

@Composable
fun VCardQrCodeScannerVm(
    userRepository: UserRepository,
    modifier: Modifier = Modifier,
    navigateToSettingsScreen: () -> Unit,
    onBackClicked: () -> Unit,
) {
    val viewModel: VCardQrCodeScannerViewModel = viewModel(
        factory = VCardQrCodeScannerViewModel.Factory.create(userRepository)
    )
    VCardQrCodeScanner(
        modifier = modifier,
        navigateToSettingsScreen = navigateToSettingsScreen,
        onRefusePermissionClicked = onBackClicked,
        onQrCodeDetected = {
            viewModel.saveNetworkingProfile(user = it.first())
            onBackClicked()
        },
        onBackClicked = onBackClicked
    )
}

@Composable
fun VCardQrCodeScanner(
    modifier: Modifier = Modifier,
    navigateToSettingsScreen: () -> Unit,
    onRefusePermissionClicked: () -> Unit,
    onQrCodeDetected: (List<UserNetworkingUi>) -> Unit,
    onBackClicked: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.screen_qrcode_scanner),
                navigationIcon = { Back(onClick = onBackClicked) }
            )
        },
        content = {
            Box(modifier = Modifier.padding(it)) {
                FeatureThatRequiresCameraPermission(
                    navigateToSettingsScreen = navigateToSettingsScreen,
                    onRefusePermissionClicked = onRefusePermissionClicked,
                    content = {
                        VCardCameraPreview(onQrCodeDetected = onQrCodeDetected)
                    }
                )
            }
        }
    )
}
