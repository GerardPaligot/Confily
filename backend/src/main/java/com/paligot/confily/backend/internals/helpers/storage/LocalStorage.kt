package com.paligot.confily.backend.internals.helpers.storage

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class LocalStorage(
    private val directory: String,
    private val location: String = "/tmp",
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : Storage {
    override suspend fun download(filename: String): ByteArray? = withContext(dispatcher) {
        File("$location/$directory/$filename").readBytes()
    }

    override suspend fun upload(
        filename: String,
        content: ByteArray,
        mimeType: MimeType
    ) = withContext(dispatcher) {
        File("$location/$filename").apply {
            writeBytes(content)
            createNewFile()
        }
        return@withContext Upload(
            bucketName = directory,
            filename = filename,
            url = "$location/$directory/$filename"
        )
    }

    override suspend fun delete(filename: String): Unit = withContext(dispatcher) {
        File("$location/$directory/$filename").delete()
    }
}
