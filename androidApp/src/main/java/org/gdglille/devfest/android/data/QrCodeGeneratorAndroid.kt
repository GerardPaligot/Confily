package org.gdglille.devfest.android.data

import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import org.gdglille.devfest.Image
import org.gdglille.devfest.repositories.QrCodeGenerator

class QrCodeGeneratorAndroid : QrCodeGenerator {
    override fun generate(text: String): Image {
        val barcodeEncoder = BarcodeEncoder()
        return barcodeEncoder.encodeBitmap(text, BarcodeFormat.QR_CODE, 512, 512)
    }
}
