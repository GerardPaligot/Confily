package org.gdglille.devfest.backend.internals.helpers.storage

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class LocalStorage(
    private val directory: String,
    private val location: String = "/tmp",
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : Storage {
    override suspend fun upload(filename: String, content: ByteArray) = withContext(dispatcher) {
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
}
