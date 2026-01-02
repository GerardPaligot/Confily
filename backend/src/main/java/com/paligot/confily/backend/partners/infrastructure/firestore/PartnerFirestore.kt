package com.paligot.confily.backend.partners.infrastructure.firestore

import com.google.cloud.firestore.Firestore
import com.paligot.confily.backend.internals.helpers.database.docExists
import com.paligot.confily.backend.internals.helpers.database.insert
import com.paligot.confily.backend.internals.helpers.database.isNotEmpty
import com.paligot.confily.backend.internals.helpers.database.map
import com.paligot.confily.backend.internals.helpers.database.upsert

private const val CollectionName = "companies"

class PartnerFirestore(
    private val projectName: String,
    private val firestore: Firestore
) {
    fun getAll(eventId: String): List<PartnerEntity> = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName)
        .map<PartnerEntity, PartnerEntity> {
            if (it.siteUrl.contains(Regex("^http[s]{0,1}://"))) return@map it
            return@map it.copy(siteUrl = "https://${it.siteUrl}")
        }

    fun exists(eventId: String, partnerId: String): Boolean = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName)
        .docExists(partnerId)

    fun createOrUpdate(eventId: String, partner: PartnerEntity): String {
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

    fun hasPartners(eventId: String): Boolean = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName)
        .isNotEmpty()
}
