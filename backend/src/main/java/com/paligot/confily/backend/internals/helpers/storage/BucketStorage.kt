package com.paligot.confily.backend.internals.helpers.storage

import com.google.cloud.storage.Acl
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.StorageException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.google.cloud.storage.Storage as CloudStorage

class BucketStorage(
    private val storage: CloudStorage,
    private val bucketName: String,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : Storage {
    @Suppress("SwallowedException")
    override suspend fun download(filename: String): ByteArray? = withContext(dispatcher) {
        val blobId = BlobId.of(bucketName, filename)
        return@withContext try {
            storage.readAllBytes(blobId)
        } catch (ex: StorageException) {
            null
        }
    }

    override suspend fun upload(filename: String, content: ByteArray, mimeType: MimeType): Upload =
        withContext(dispatcher) {
            val blobId = BlobId.of(bucketName, filename)
            val blobInfo = BlobInfo
                .newBuilder(blobId)
                .setAcl(arrayListOf(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER)))
                .setCacheControl("public")
                .setContentType(mimeType.value)
                .build()
            storage.create(blobInfo, content)
            return@withContext Upload(
                bucketName = bucketName,
                filename = filename,
                url = "https://storage.googleapis.com/$bucketName/$filename"
            )
        }

    override suspend fun delete(filename: String): Unit = withContext(dispatcher) {
        storage.delete(bucketName, filename)
    }
}
