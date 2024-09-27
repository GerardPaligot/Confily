package com.paligot.confily.core

import android.content.Context
import okio.FileSystem
import okio.Path.Companion.toPath

data class AndroidContext(val context: Context)
actual typealias PlatformContext = AndroidContext

actual class Platform actual constructor(context: PlatformContext) {
    actual val fileEngine = FileEngine(
        fileSystem = FileSystem.SYSTEM,
        tempFolderPath = context.context.cacheDir?.absolutePath?.toPath()
            ?: FileSystem.SYSTEM_TEMPORARY_DIRECTORY
    )
    actual val hasSupportSVG: Boolean = true
}

actual class DecimalFormat {
    actual fun format(number: Int): String {
        val formatter = java.text.DecimalFormat()
        formatter.minimumIntegerDigits = 2
        formatter.isGroupingUsed = false
        formatter.maximumFractionDigits = 2
        formatter.isDecimalSeparatorAlwaysShown = false
        return formatter.format(number)
    }
}
