package org.gdglille.devfest.backend.partners

import kotlinx.coroutines.coroutineScope
import org.gdglille.devfest.backend.database.Database
import org.gdglille.devfest.backend.database.get
import org.gdglille.devfest.backend.database.getAll
import org.gdglille.devfest.backend.database.query
import org.gdglille.devfest.backend.database.whereEquals
import org.gdglille.devfest.backend.database.whereNotEquals

enum class Sponsorship { Gold, Silver, Bronze, Other }

class PartnerDao(private val database: Database) {
    suspend fun listValidatedFromCms4Partners(
        year: String,
        sponsorship: Sponsorship
    ): List<PartnerDb> = database
        .query<PartnerDb>(
            "edition".whereEquals(year),
            "public".whereEquals(true),
            "logoUrl".whereNotEquals(null),
            "sponsoring".whereEquals(sponsorship.name)
        )
        .map {
            if (it.siteUrl == null) return@map it
            if (it.siteUrl.contains(Regex("^http[s]{0,1}://"))) return@map it
            return@map it.copy(siteUrl = "https://${it.siteUrl}")
        }

    suspend fun getAll(eventId: String): List<PartnerDb> = database
        .getAll<PartnerDb>(eventId)
        .map {
            if (it.siteUrl == null) return@map it
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
}