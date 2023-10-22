package org.gdglille.devfest

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toCValues
import kotlinx.cinterop.usePinned
import okio.FileSystem
import org.gdglille.devfest.models.ui.Image
import platform.Foundation.NSBundle
import platform.Foundation.NSData
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.dataWithBytes
import platform.Foundation.stringWithFormat
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
    actual fun getString(key: String): String = key.localized()
    actual fun getString(key: String, count: Int, args: List<Any>): String =
        key.localized(count, *args.toTypedArray())
    actual fun getString(key: String, args: List<Any>): String =
        key.localized(*args.toTypedArray())
}

fun String.localized(): String {
    val localizedString = NSBundle.mainBundle.localizedStringForKey(this, this, null)
    if (localizedString != this) return localizedString

    val baseResourcePath = NSBundle.mainBundle.pathForResource("Base", "lproj")
        ?.let { NSURL(fileURLWithPath = it) }
    val baseBundle = baseResourcePath?.let { NSBundle(it) }

    if (baseBundle != null) {
        val baseString = baseBundle.localizedStringForKey(this, this, null)
        if (baseString != this) return baseString
    }
    return this
}

fun String.localized(count: Int, vararg arguments: Any?): String {
    val quantity = when (count) {
        0 -> "zero"
        1 -> "one"
        2 -> "two"
        else -> "other"
    }
    return "$this.$quantity".localized(arguments)
}

fun String.localized(vararg arguments: Any?): String {
    val format = localized()
    // Shorten the variable name
    val a = arguments
    // Kotlin does not support passing variadic parameters to Objective-C
    // We implement calling the method with up to 9 arguments which is enough in practice
    return when (arguments.size) {
        0 -> NSString.stringWithFormat(format)
        1 -> NSString.stringWithFormat(format, a[0])
        2 -> NSString.stringWithFormat(format, a[0], a[1])
        3 -> NSString.stringWithFormat(format, a[0], a[1], a[2])
        4 -> NSString.stringWithFormat(format, a[0], a[1], a[2], a[3])
        5 -> NSString.stringWithFormat(format, a[0], a[1], a[2], a[3], a[4])
        6 -> NSString.stringWithFormat(format, a[0], a[1], a[2], a[3], a[4], a[5])
        7 -> NSString.stringWithFormat(format, a[0], a[1], a[2], a[3], a[4], a[5], a[6])
        8 -> NSString.stringWithFormat(format, a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7])
        9 -> NSString.stringWithFormat(format, a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8])
        else -> error("Too many arguments.")
    }
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

@OptIn(ExperimentalForeignApi::class)
actual fun ByteArray.toNativeImage(): Image = memScoped {
    toCValues()
        .ptr
        .let { NSData.dataWithBytes(it, size.toULong()) }
        .let { UIImage.imageWithData(it) }!!
}

@OptIn(ExperimentalForeignApi::class)
actual fun Image.toByteArray(): ByteArray {
    val data = UIImageJPEGRepresentation(this, 1.0)!!
    return ByteArray(data.length.toInt()).apply {
        usePinned {
            memcpy(it.addressOf(0), data.bytes, data.length)
        }
    }
}
