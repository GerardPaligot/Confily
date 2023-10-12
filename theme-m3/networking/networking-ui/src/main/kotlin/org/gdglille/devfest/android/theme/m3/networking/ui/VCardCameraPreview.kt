package org.gdglille.devfest.android.theme.m3.networking.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.gdglille.devfest.models.ui.VCardModel
import org.gdglille.devfest.android.ui.camera.CameraPreview

@Composable
fun VCardCameraPreview(
    onQrCodeDetected: (List<VCardModel>) -> Unit,
    modifier: Modifier = Modifier
) {
    val qrCodeDetected = remember { mutableStateOf(false) }
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
                    }
                )
                qrCodeDetected.value = !qrCodeDetected.value
            }
        },
        modifier = modifier
    )
}
