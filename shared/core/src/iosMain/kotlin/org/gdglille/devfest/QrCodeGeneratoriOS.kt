package org.gdglille.devfest

import kotlinx.cinterop.ExperimentalForeignApi
import org.gdglille.devfest.models.ui.Image
import org.gdglille.devfest.repositories.QrCodeGenerator
import platform.CoreGraphics.CGColorSpaceCreateDeviceRGB
import platform.CoreImage.CIContext
import platform.CoreImage.CIFilter
import platform.CoreImage.JPEGRepresentationOfImage
import platform.CoreImage.QRCodeGenerator
import platform.Foundation.setValue
import platform.UIKit.UIImage

class QrCodeGeneratoriOS : QrCodeGenerator {
    @OptIn(ExperimentalForeignApi::class)
    override fun generate(text: String): Image {
        val filter = CIFilter.QRCodeGenerator().apply {
            setValue(text, forKey = "inputMessage")
        }
        val outputImg = filter.outputImage ?: return UIImage()
        val nsData = CIContext().JPEGRepresentationOfImage(
            outputImg,
            CGColorSpaceCreateDeviceRGB(),
            mapOf<Any?, String>()
        ) ?: return UIImage()
        return UIImage(nsData)
    }
}
