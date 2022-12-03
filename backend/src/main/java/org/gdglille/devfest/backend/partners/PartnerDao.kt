package org.gdglille.devfest.backend.partners

import kotlinx.coroutines.coroutineScope
import org.gdglille.devfest.backend.database.Database
import org.gdglille.devfest.backend.database.get
import org.gdglille.devfest.backend.database.getAll

class PartnerDao(private val database: Database) {
    suspend fun getAll(eventId: String): List<PartnerDb> = database
        .getAll<PartnerDb>(eventId)
        .map {
            if (it.siteUrl.contains(Regex("^http[s]{0,1}://"))) return@map it
            return@map it.copy(siteUrl = "https://${it.siteUrl}")
        }

    suspend fun createOrUpdate(eventId: String, partner: PartnerDb): String = coroutineScope {
        if (partner.id == "") return@coroutineScope database.insert(eventId) { partner.copy(id = it) }
        val existing = database.get<PartnerDb>(eventId, partner.id)
        if (existing == null) database.insert(eventId, partner.id, partner)
        else database.update(eventId, partner.id, partner)
        return@coroutineScope partner.id
    }

    suspend fun hasPartners(): Boolean = database.count() > 0
}
