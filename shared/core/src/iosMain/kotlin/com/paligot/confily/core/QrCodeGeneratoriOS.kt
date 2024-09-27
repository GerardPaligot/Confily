package com.paligot.confily.core

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.CoreGraphics.CGColorSpaceCreateDeviceRGB
import platform.CoreImage.CIContext
import platform.CoreImage.CIFilter
import platform.CoreImage.CIImage
import platform.CoreImage.JPEGRepresentationOfImage
import platform.CoreImage.QRCodeGenerator
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.dataUsingEncoding
import platform.Foundation.setValue
import platform.posix.memcpy

class QrCodeGeneratoriOS : QrCodeGenerator {
    @OptIn(ExperimentalForeignApi::class)
    override fun generate(text: String): ByteArray {
        val filter: CIFilter = CIFilter.QRCodeGenerator().apply {
            setValue(
                value = (text as NSString).dataUsingEncoding(NSUTF8StringEncoding),
                forKey = "inputMessage"
            )
        }
        val outputImg: CIImage = filter.outputImage ?: return byteArrayOf()
        val nsData: NSData = CIContext().JPEGRepresentationOfImage(
            outputImg,
            CGColorSpaceCreateDeviceRGB(),
            mapOf<Any?, String>()
        ) ?: return byteArrayOf()
        return nsData.toByteArray()
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun NSData.toByteArray(): ByteArray = ByteArray(this@toByteArray.length.toInt()).apply {
    usePinned {
        memcpy(it.addressOf(0), this@toByteArray.bytes, this@toByteArray.length)
    }
}
