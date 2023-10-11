package org.gdglille.devfest

import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import org.gdglille.devfest.repositories.QrCodeGenerator

class QrCodeGeneratorAndroid : QrCodeGenerator {
    override fun generate(text: String): Image {
        val barcodeEncoder = BarcodeEncoder()
        return barcodeEncoder.encodeBitmap(text, BarcodeFormat.QR_CODE, QrcodeSize, QrcodeSize)
    }

    companion object {
        private const val QrcodeSize = 512
    }
}
