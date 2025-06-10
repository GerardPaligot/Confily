package com.paligot.confily.backend.internals.infrastructure.storage

import com.paligot.confily.backend.internals.helpers.storage.MimeType
import com.paligot.confily.backend.internals.helpers.storage.Storage
import com.paligot.confily.backend.internals.helpers.storage.Upload
import com.paligot.confily.backend.internals.infrastructure.transcoder.Png
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class PartnerStorage(
    private val storage: Storage
) {
    suspend fun uploadPartnerLogos(
        eventId: String,
        partnerId: String,
        pngs: List<Png>
    ): List<Upload> = coroutineScope {
        return@coroutineScope pngs
            .filter { it.content != null }
            .map { png ->
                async {
                    storage.upload(
                        filename = "$eventId/partners/$partnerId/${png.size}.png",
                        content = png.content!!,
                        mimeType = MimeType.PNG
                    )
                }
            }
            .awaitAll()
    }
}
