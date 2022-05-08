package org.gdglille.devfest.backend.partners

import kotlinx.coroutines.coroutineScope
import org.gdglille.devfest.backend.database.*

enum class Sponsorship { Gold, Silver, Bronze, Other }

class PartnerDao(private val database: Database) {
    suspend fun listValidatedFromCms4Partners(year: String, sponsorship: Sponsorship): List<PartnerDb> = database.query(
        "edition".whereEquals(year),
        "public".whereEquals(true),
        "logoUrl".whereNotEquals(null),
        "sponsoring".whereEquals(sponsorship.name)
    )

    suspend fun getAll(eventId: String): List<PartnerDb> = database.getAll(eventId)

    suspend fun createOrUpdate(eventId: String, partner: PartnerDb): String = coroutineScope {
        if (partner.id == "") return@coroutineScope database.insert(eventId) { partner.copy(id = it) }
        val existing = database.get<PartnerDb>(eventId, partner.id)
        if (existing == null) database.insert(eventId, partner.id, partner)
        else database.update(eventId, partner.id, partner)
        return@coroutineScope partner.id
    }
}