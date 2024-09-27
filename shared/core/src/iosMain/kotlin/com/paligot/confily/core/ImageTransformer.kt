package com.paligot.confily.core

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import platform.Foundation.NSData
import platform.Foundation.create
import platform.UIKit.UIImage

object ImageTransformer {
    @OptIn(ExperimentalForeignApi::class)
    fun ByteArray.toUIImage(): UIImage = memScoped {
        NSData.create(
            bytes = allocArrayOf(this@toUIImage),
            length = this@toUIImage.size.toULong()
        )
            .let { UIImage.imageWithData(it) }!!
    }
}
