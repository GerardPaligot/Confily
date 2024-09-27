package com.paligot.confily.core

import okio.FileSystem
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter

class IOSContext
actual typealias PlatformContext = IOSContext

actual class Platform actual constructor(context: PlatformContext) {
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
