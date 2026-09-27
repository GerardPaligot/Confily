package com.paligot.confily.backend.internals.helpers.storage

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.storage.storage
import io.ktor.http.ContentType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SupabaseStorage(
    private val client: SupabaseClient,
    private val bucketName: String,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : Storage {
    private val bucket get() = client.storage.from(bucketName)

    @Suppress("SwallowedException")
    override suspend fun download(filename: String): ByteArray? = withContext(dispatcher) {
        return@withContext try {
            bucket.downloadPublic(filename)
        } catch (ex: RestException) {
            null
        }
    }

    override suspend fun upload(filename: String, content: ByteArray, mimeType: MimeType): Upload =
        withContext(dispatcher) {
            bucket.upload(filename, content) {
                upsert = true
                contentType = ContentType.parse(mimeType.value)
            }
            return@withContext Upload(
                bucketName = bucketName,
                filename = filename,
                url = bucket.publicUrl(filename)
            )
        }

    override suspend fun delete(filename: String): Unit = withContext(dispatcher) {
        bucket.delete(filename)
    }
}
