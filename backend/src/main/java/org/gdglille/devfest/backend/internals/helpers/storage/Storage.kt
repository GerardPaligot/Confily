package org.gdglille.devfest.backend.internals.helpers.storage

import com.google.cloud.storage.Storage as CloudStorage

interface Storage {
    suspend fun upload(filename: String, content: ByteArray): Upload

    object Factory {
        fun create(storage: CloudStorage, bucketName: String, isAppEngine: Boolean): Storage {
            return if (isAppEngine) BucketStorage(storage, bucketName)
            else LocalStorage(bucketName)
        }
    }
}

data class Upload(
    val bucketName: String,
    val filename: String,
    val url: String
)
