package com.paligot.confily.backend.internals.infrastructure.storage

import com.paligot.confily.backend.internals.helpers.storage.MimeType
import com.paligot.confily.backend.internals.helpers.storage.Storage

class TeamStorage(
    private val storage: Storage
) {
    suspend fun saveTeamPicture(eventId: String, id: String, content: ByteArray, mimeType: MimeType) =
        storage.upload(
            filename = "$eventId/team/$id.${mimeType.extension}",
            content = content,
            mimeType = mimeType
        )
}
