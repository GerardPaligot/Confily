package com.paligot.confily.backend.partners

import com.google.cloud.firestore.Firestore
import com.paligot.confily.backend.internals.helpers.database.docExists
import com.paligot.confily.backend.internals.helpers.database.insert
import com.paligot.confily.backend.internals.helpers.database.isNotEmpty
import com.paligot.confily.backend.internals.helpers.database.map
import com.paligot.confily.backend.internals.helpers.database.upsert
import com.paligot.confily.backend.internals.helpers.image.Png
import com.paligot.confily.backend.internals.helpers.storage.MimeType
import com.paligot.confily.backend.internals.helpers.storage.Storage
import com.paligot.confily.backend.internals.helpers.storage.Upload
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

private const val CollectionName = "companies"

class PartnerDao(
    private val projectName: String,
    private val firestore: Firestore,
    private val storage: Storage
) {
    fun getAll(eventId: String): List<PartnerDb> = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName)
        .map<PartnerDb, PartnerDb> {
            if (it.siteUrl.contains(Regex("^http[s]{0,1}://"))) return@map it
            return@map it.copy(siteUrl = "https://${it.siteUrl}")
        }

    fun exists(eventId: String, partnerId: String): Boolean = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName)
        .docExists(partnerId)

    fun createOrUpdate(eventId: String, partner: PartnerDb): String {
        if (partner.id == "") {
            return firestore
                .collection(projectName)
                .document(eventId)
                .collection(CollectionName)
                .insert { partner.copy(id = it) }
        }
        firestore
            .collection(projectName)
            .document(eventId)
            .collection(CollectionName)
            .upsert(partner.id, partner)
        return partner.id
    }

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

    fun hasPartners(eventId: String): Boolean = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName)
        .isNotEmpty()
}
