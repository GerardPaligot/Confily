package com.paligot.confily.backend.internals.helpers.storage

import com.google.cloud.storage.Storage as CloudStorage

interface Storage {
    suspend fun download(filename: String): ByteArray?
    suspend fun upload(filename: String, content: ByteArray, mimeType: MimeType): Upload
    suspend fun delete(filename: String)

    object Factory {
        fun create(storage: CloudStorage, bucketName: String, isAppEngine: Boolean): Storage {
            return if (isAppEngine) {
                BucketStorage(storage, bucketName)
            } else {
                LocalStorage(bucketName)
            }
        }
    }
}

enum class MimeType(val value: String, val extension: String) {
    PNG("image/png", "png"),
    JPEG("image/jpeg", "jpeg"),
    GIF("image/gif", "gif"),
    SVG("image/svg+xml", "svg"),
    WEBP("image/webp", "webp"),
    JSON("application/json", "json"),
    OCTET_STREAM("application/octet-stream", "bin")
}

data class Upload(
    val bucketName: String,
    val filename: String,
    val url: String
)
