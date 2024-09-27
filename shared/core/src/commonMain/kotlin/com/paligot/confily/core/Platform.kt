package com.paligot.confily.core

import okio.FileSystem
import okio.Path

data class FileEngine(
    val fileSystem: FileSystem,
    val tempFolderPath: Path
)

expect class PlatformContext

expect class Platform(context: PlatformContext) {
    val fileEngine: FileEngine
    val hasSupportSVG: Boolean
}

expect class DecimalFormat() {
    fun format(number: Int): String
}
