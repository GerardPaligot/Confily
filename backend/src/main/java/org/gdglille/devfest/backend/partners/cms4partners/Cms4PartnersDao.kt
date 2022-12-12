package org.gdglille.devfest.backend.partners.cms4partners

import org.gdglille.devfest.backend.internals.helpers.database.BasicDatabase
import org.gdglille.devfest.backend.internals.helpers.database.whereEquals
import org.gdglille.devfest.backend.internals.helpers.database.whereNotEquals

enum class Sponsorship { Gold, Silver, Bronze, Other }

class Cms4PartnersDao(
    private val database: BasicDatabase
) {
    suspend fun list(year: String, sponsorship: Sponsorship): List<Cms4PartnerDb> {
        return database
            .query(
                collectionName = "companies",
                clazz = Cms4PartnerDb::class,
                "edition".whereEquals(year),
                "public".whereEquals(true),
                "logoUrl".whereNotEquals(null),
                "sponsoring".whereEquals(sponsorship.name)
            )
            .map {
                val partner = it.second.copy(id = it.first)
                if (partner.siteUrl == null) return@map partner
                if (partner.siteUrl.contains(Regex("^https?://"))) return@map partner
                return@map partner.copy(siteUrl = "https://${partner.siteUrl}")
            }
    }

    suspend fun list(year: String): List<Cms4PartnerDb> {
        val collectionName = if (hasPartners(year)) {
            "companies-$year"
        } else {
            "companies"
        }
        return database
            .query(
                collectionName = collectionName,
                clazz = Cms4PartnerDb::class,
                "edition".whereEquals(year),
                "public".whereEquals(true),
                "logoUrl".whereNotEquals(null)
            )
            .map {
                val partner = it.second.copy(id = it.first)
                if (partner.siteUrl == null) return@map partner
                if (partner.siteUrl.contains(Regex("^https?://"))) return@map partner
                return@map partner.copy(siteUrl = "https://${partner.siteUrl}")
            }
    }

    suspend fun hasPartners(year: String): Boolean = database.count(collectionName = "companies-$year") > 0
}
