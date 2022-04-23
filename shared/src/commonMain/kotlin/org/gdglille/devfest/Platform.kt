package org.gdglille.devfest

import io.ktor.client.engine.*

expect class Platform() {
    val platform: String
    val engine: HttpClientEngine
}

expect class DecimalFormat() {
    fun format(number: Int): String
}

expect class Image
expect fun ByteArray.toNativeImage(): Image
expect fun Image.toByteArray(): ByteArray
