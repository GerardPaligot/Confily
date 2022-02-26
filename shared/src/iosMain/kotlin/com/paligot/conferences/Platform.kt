package com.paligot.conferences

import io.ktor.client.engine.*
import io.ktor.client.engine.darwin.*
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toCValues
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.dataWithBytes
import platform.UIKit.UIDevice
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.posix.memcpy

actual class Platform actual constructor() {
    actual val platform: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    actual val engine: HttpClientEngine = Darwin.create()
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
