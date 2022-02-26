package com.paligot.conferences.android.data

import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.paligot.conferences.Image
import com.paligot.conferences.repositories.QrCodeGenerator

class QrCodeGeneratorAndroid: QrCodeGenerator {
    override fun generate(text: String): Image {
        val barcodeEncoder = BarcodeEncoder()
        return barcodeEncoder.encodeBitmap(text, BarcodeFormat.QR_CODE, 512, 512)
    }
}