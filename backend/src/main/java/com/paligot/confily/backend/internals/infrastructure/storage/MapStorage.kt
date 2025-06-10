package com.paligot.confily.backend.internals.infrastructure.storage

import com.paligot.confily.backend.internals.helpers.storage.MimeType
import com.paligot.confily.backend.internals.helpers.storage.Storage
import com.paligot.confily.backend.internals.helpers.storage.Upload

class MapStorage(
    private val storage: Storage
) {
    suspend fun uploadMap(
        eventId: String,
        mapId: String,
        content: ByteArray
    ): Upload = storage.upload(
        filename = "$eventId/maps/$mapId",
        content = content,
        mimeType = MimeType.PNG
    )

    suspend fun delete(eventId: String, mapId: String) {
        storage.delete("$eventId/maps/$mapId")
    }
}
