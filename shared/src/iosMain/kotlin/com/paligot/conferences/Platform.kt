package com.paligot.conferences

import io.ktor.client.engine.*
import io.ktor.client.engine.darwin.*
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toCValues
import platform.Foundation.NSData
import platform.Foundation.dataWithBytes
import platform.UIKit.UIDevice
import platform.UIKit.UIImage

actual class Platform actual constructor() {
    actual val platform: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    actual val engine: HttpClientEngine = Darwin.create()
}

actual typealias Image = UIImage
actual fun ByteArray.toNativeImage(): Image =
    memScoped {
        toCValues()
            .ptr
            .let { NSData.dataWithBytes(it, size.toULong()) }
            .let { UIImage.imageWithData(it) }
    }
