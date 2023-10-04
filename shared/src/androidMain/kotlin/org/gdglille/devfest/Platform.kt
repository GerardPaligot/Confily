package org.gdglille.devfest

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android
import okio.FileSystem
import okio.Path.Companion.toPath
import java.io.ByteArrayOutputStream

data class AndroidContext(val context: Context)
actual typealias PlatformContext = AndroidContext

actual class Platform actual constructor(private val context: PlatformContext) {
    actual val httpEngine: HttpClientEngine = Android.create()
    actual val fileEngine = FileEngine(
        fileSystem = FileSystem.SYSTEM,
        tempFolderPath = context.context.cacheDir?.absolutePath?.toPath()
            ?: FileSystem.SYSTEM_TEMPORARY_DIRECTORY
    )
    actual val hasSupportSVG: Boolean = true
    actual fun getString(key: String): String {
        val androidContext = context.context
        val resourceId = androidContext.resources.getIdentifier(key, "string", androidContext.packageName)
        if (resourceId == 0) return key
        return androidContext.getString(resourceId)
    }
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
