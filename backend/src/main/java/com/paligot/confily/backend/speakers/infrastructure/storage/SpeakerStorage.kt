package com.paligot.confily.backend.speakers.infrastructure.storage

import com.paligot.confily.backend.internals.helpers.storage.MimeType
import com.paligot.confily.backend.internals.helpers.storage.Storage
import com.paligot.confily.backend.internals.helpers.storage.Upload

class SpeakerStorage(
    private val storage: Storage
) {
    suspend fun saveProfile(eventId: String, id: String, content: ByteArray, mimeType: MimeType): Upload =
        storage.upload(
            filename = "$eventId/speakers/$id.png",
            content = content,
            mimeType = mimeType
        )
}
