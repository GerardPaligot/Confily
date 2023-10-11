package org.gdglille.devfest.android.theme.vitamin.ui.screens.networking

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.gdglille.devfest.android.data.models.VCardModel
import org.gdglille.devfest.android.theme.vitamin.ui.components.appbars.TopAppBar
import org.gdglille.devfest.android.theme.vitamin.ui.components.permissions.FeatureThatRequiresCameraPermission
import org.gdglille.devfest.android.ui.camera.CameraPreview
import org.gdglille.devfest.android.ui.resources.R

@Composable
fun VCardQrCodeScanner(
    navigateToSettingsScreen: () -> Unit,
    onQrCodeDetected: (VCardModel) -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val qrCodeDetected = remember { mutableStateOf(false) }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.screen_qrcode_scanner),
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
                        CameraPreview(
                            onBarcodeScanned = { barcodes ->
                                val filtered = barcodes.filter { it.contactInfo != null }
                                if (filtered.isNotEmpty() && !qrCodeDetected.value) {
                                    onQrCodeDetected(
                                        filtered.map {
                                            VCardModel(
                                                email = it.contactInfo?.emails?.first()?.address ?: "",
                                                firstName = it.contactInfo?.name?.first ?: "",
                                                lastName = it.contactInfo?.name?.last ?: "",
                                                company = it.contactInfo?.organization ?: ""
                                            )
                                        }.first()
                                    )
                                    qrCodeDetected.value = !qrCodeDetected.value
                                }
                            }
                        )
                    }
                )
            }
        }
    )
}
