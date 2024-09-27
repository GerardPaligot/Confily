package com.paligot.confily.core

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.ByteArrayOutputStream

class QrCodeGeneratorAndroid : QrCodeGenerator {
    override fun generate(text: String): ByteArray {
        val barcodeEncoder = BarcodeEncoder()
        return barcodeEncoder.encodeBitmap(text, BarcodeFormat.QR_CODE, QrcodeSize, QrcodeSize)
            .toByteArray()
    }

    private fun Bitmap.toByteArray(): ByteArray {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.PNG, 90, stream)
        return stream.toByteArray()
    }

    companion object {
        private const val QrcodeSize = 512
    }
}
