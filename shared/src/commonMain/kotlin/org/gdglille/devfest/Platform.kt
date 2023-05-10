package org.gdglille.devfest

import io.ktor.client.engine.HttpClientEngine
import okio.FileSystem
import okio.Path

data class FileEngine(
    val fileSystem: FileSystem,
    val tempFolderPath: Path
)

expect class PlatformContext

expect class Platform(context: PlatformContext) {
    val httpEngine: HttpClientEngine
    val fileEngine: FileEngine
    val hasSupportSVG: Boolean
}

expect class DecimalFormat() {
    fun format(number: Int): String
}

expect class Image

expect fun ByteArray.toNativeImage(): Image
expect fun Image.toByteArray(): ByteArray
