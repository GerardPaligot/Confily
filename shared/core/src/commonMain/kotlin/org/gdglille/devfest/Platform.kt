package org.gdglille.devfest

import io.ktor.client.engine.HttpClientEngine
import okio.FileSystem
import okio.Path
import org.gdglille.devfest.models.ui.Image

data class FileEngine(
    val fileSystem: FileSystem,
    val tempFolderPath: Path
)

expect class PlatformContext

expect class Platform(context: PlatformContext) {
    val httpEngine: HttpClientEngine
    val fileEngine: FileEngine
    val hasSupportSVG: Boolean
    fun getString(key: String): String
    fun getString(key: String, count: Int, args: List<Any>): String
    fun getString(key: String, args: List<Any>): String
}

expect class DecimalFormat() {
    fun format(number: Int): String
}

expect fun ByteArray.toNativeImage(): Image
expect fun Image.toByteArray(): ByteArray
