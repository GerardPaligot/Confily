package org.gdglille.devfest

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android
import java.io.ByteArrayOutputStream

actual class Platform actual constructor() {
    actual val platform: String = "Android ${android.os.Build.VERSION.SDK_INT}"
    actual val engine: HttpClientEngine = Android.create()
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

actual typealias Image = Bitmap
actual fun ByteArray.toNativeImage(): Image = BitmapFactory.decodeByteArray(this, 0, this.size)
actual fun Image.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.PNG, 90, stream)
    return stream.toByteArray()
}
