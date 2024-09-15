package com.paligot.confily.backend.internals

import com.paligot.confily.backend.internals.helpers.storage.MimeType
import java.util.Locale

fun String.slug() = lowercase(Locale.ROOT)
    .replace("\n", " ")
    .replace("[^a-z\\d\\s]".toRegex(), " ")
    .split(" ")
    .joinToString("-")
    .replace("-+".toRegex(), "-")

val String.mimeType: MimeType
    get() = when (substringAfterLast(".")) {
        "png" -> MimeType.PNG
        "jpeg", "jpg" -> MimeType.JPEG
        "gif" -> MimeType.GIF
        "svg" -> MimeType.SVG
        "webp" -> MimeType.WEBP
        else -> MimeType.OCTET_STREAM
    }
