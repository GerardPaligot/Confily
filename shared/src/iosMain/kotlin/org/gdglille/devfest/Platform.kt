package org.gdglille.devfest

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toCValues
import kotlinx.cinterop.usePinned
import okio.FileSystem
import platform.Foundation.NSData
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.dataWithBytes
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.posix.memcpy

class IOSContext
actual typealias PlatformContext = IOSContext

actual class Platform actual constructor(context: PlatformContext) {
    actual val httpEngine: HttpClientEngine = Darwin.create()
    actual val fileEngine = FileEngine(
        fileSystem = FileSystem.SYSTEM,
        tempFolderPath = FileSystem.SYSTEM_TEMPORARY_DIRECTORY
    )
    actual val hasSupportSVG: Boolean = false
}

actual class DecimalFormat {
    actual fun format(number: Int): String {
        val formatter = NSNumberFormatter()
        formatter.minimumFractionDigits = 0u
        formatter.maximumFractionDigits = 2u
        formatter.numberStyle = 1u
        formatter.minimumIntegerDigits = 2u
        return formatter.stringFromNumber(NSNumber(number))!!
    }
}

actual typealias Image = UIImage

actual fun ByteArray.toNativeImage(): Image = memScoped {
    toCValues()
        .ptr
        .let { NSData.dataWithBytes(it, size.toULong()) }
        .let { UIImage.imageWithData(it) }!!
}

actual fun Image.toByteArray(): ByteArray {
    val data = UIImageJPEGRepresentation(this, 1.0)!!
    return ByteArray(data.length.toInt()).apply {
        usePinned {
            memcpy(it.addressOf(0), data.bytes, data.length)
        }
    }
}
