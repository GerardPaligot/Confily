package org.gdglille.devfest.backend.internals.helpers.storage

import com.google.cloud.storage.Acl
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.google.cloud.storage.Storage as CloudStorage

class BucketStorage(
    private val storage: CloudStorage,
    private val bucketName: String,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : Storage {
    override suspend fun upload(filename: String, content: ByteArray): Upload =
        withContext(dispatcher) {
            val blobId = BlobId.of(bucketName, filename)
            val blobInfo = BlobInfo
                .newBuilder(blobId)
                .setAcl(arrayListOf(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER)))
                .build()
            storage.create(blobInfo, content)
            return@withContext Upload(
                bucketName = bucketName,
                filename = filename,
                url = "https://storage.googleapis.com/$bucketName/$filename"
            )
        }
}
