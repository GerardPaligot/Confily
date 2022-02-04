package com.paligot.conferences

import io.ktor.client.engine.*

expect class Platform() {
    val platform: String
    val engine: HttpClientEngine
}

expect class Image
expect fun ByteArray.toNativeImage(): Image
