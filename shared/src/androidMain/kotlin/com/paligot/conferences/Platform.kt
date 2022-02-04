package com.paligot.conferences

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.ktor.client.engine.*
import io.ktor.client.engine.android.*

actual class Platform actual constructor() {
    actual val platform: String = "Android ${android.os.Build.VERSION.SDK_INT}"
    actual val engine: HttpClientEngine = Android.create()
}

actual typealias Image = Bitmap
actual fun ByteArray.toNativeImage(): Image = BitmapFactory.decodeByteArray(this, 0, this.size)
