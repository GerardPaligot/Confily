package com.paligot.conferences.backend.users

import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.qrcode.QRCodeWriter
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

class QrCodeGenerator {
    fun getQrCodeBytes(content: String): ByteArray {
        val size = 512 //pixels
        val bits = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size)
        val image: BufferedImage = MatrixToImageWriter.toBufferedImage(bits)
        val stream = ByteArrayOutputStream()
        ImageIO.write(image, "png", stream)
        return stream.toByteArray()
    }
}