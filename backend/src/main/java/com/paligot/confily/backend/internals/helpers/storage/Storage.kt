package com.paligot.confily.backend.internals.helpers.storage

import com.google.cloud.storage.Storage as CloudStorage

interface Storage {
    suspend fun download(filename: String): ByteArray?
    suspend fun upload(filename: String, content: ByteArray, mimeType: MimeType): Upload

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

enum class MimeType(val value: String) {
    PNG("image/png"),
    JPEG("image/jpeg"),
    GIF("image/gif"),
    SVG("image/svg+xml"),
    WEBP("image/webp"),
    JSON("application/json"),
    OCTET_STREAM("application/octet-stream")
}

data class Upload(
    val bucketName: String,
    val filename: String,
    val url: String
)
