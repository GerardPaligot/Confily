package com.paligot.conferences.android.screens.scanner

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.paligot.conferences.repositories.UserRepository
import com.paligot.conferences.ui.components.appbars.TopAppBar

@Composable
fun QrCodeScannerVm(
    userRepository: UserRepository,
    modifier: Modifier = Modifier,
    navigateToSettingsScreen: () -> Unit,
    onBackClicked: () -> Unit,
) {
    val viewModel: QrCodeScannerViewModel = viewModel(
        factory = QrCodeScannerViewModel.Factory.create(userRepository)
    )
    QrCodeScanner(
        modifier = modifier,
        navigateToSettingsScreen = navigateToSettingsScreen,
        onRefusePermissionClicked = onBackClicked,
        onQrCodeDetected = {
            viewModel.saveEmailNetworking(email = it.first())
            onBackClicked()
        },
        onBackClicked = onBackClicked
    )
}

@Composable
fun QrCodeScanner(
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
                title = "Qr Code Scanner",
                navigationIcon = { Back(onClick = onBackClicked) }
            )
        },
        content = {
            FeatureThatRequiresCameraPermission(
                navigateToSettingsScreen = navigateToSettingsScreen,
                onRefusePermissionClicked = onRefusePermissionClicked,
                content = {
                    CameraPreview(onQrCodeDetected = onQrCodeDetected)
                }
            )
        }
    )
}
