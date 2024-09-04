package org.gdglille.devfest

import com.paligot.confily.models.ui.Image
import kotlinx.cinterop.ExperimentalForeignApi
import org.gdglille.devfest.repositories.QrCodeGenerator
import platform.CoreGraphics.CGColorSpaceCreateDeviceRGB
import platform.CoreImage.CIContext
import platform.CoreImage.CIFilter
import platform.CoreImage.JPEGRepresentationOfImage
import platform.CoreImage.QRCodeGenerator
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.dataUsingEncoding
import platform.Foundation.setValue
import platform.UIKit.UIImage

class QrCodeGeneratoriOS : QrCodeGenerator {
    @OptIn(ExperimentalForeignApi::class)
    override fun generate(text: String): Image {
        val filter = CIFilter.QRCodeGenerator().apply {
            setValue(
                value = (text as NSString).dataUsingEncoding(NSUTF8StringEncoding),
                forKey = "inputMessage"
            )
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
