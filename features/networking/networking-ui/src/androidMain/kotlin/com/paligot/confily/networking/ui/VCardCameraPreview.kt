package com.paligot.confily.networking.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.paligot.confily.networking.ui.models.VCardModel
import com.paligot.confily.style.components.camera.CameraPreview

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
