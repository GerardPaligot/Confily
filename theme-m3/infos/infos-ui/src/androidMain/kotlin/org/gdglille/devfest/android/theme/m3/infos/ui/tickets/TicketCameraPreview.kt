package org.gdglille.devfest.android.theme.m3.infos.ui.tickets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.paligot.confily.ui.camera.CameraPreview

@Composable
fun TicketCameraPreview(
    onQrCodeDetected: (List<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    val qrCodeDetected = remember { mutableStateOf(false) }
    CameraPreview(
        onBarcodeScanned = { barcodes ->
            val filtered = barcodes.filter { it.rawValue != null }
            if (filtered.isNotEmpty() && !qrCodeDetected.value) {
                onQrCodeDetected(filtered.map { it.rawValue!! })
                qrCodeDetected.value = !qrCodeDetected.value
            }
        },
        modifier = modifier
    )
}
