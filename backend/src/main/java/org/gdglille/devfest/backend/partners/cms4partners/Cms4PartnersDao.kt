package org.gdglille.devfest.backend.partners.cms4partners

import org.gdglille.devfest.backend.internals.helpers.database.BasicDatabase
import org.gdglille.devfest.backend.internals.helpers.database.query
import org.gdglille.devfest.backend.internals.helpers.database.whereEquals
import org.gdglille.devfest.backend.internals.helpers.database.whereNotEquals

enum class Sponsorship { Gold, Silver, Bronze, Other }

class Cms4PartnersDao(
    private val database: BasicDatabase
) {
    suspend fun list(year: String, sponsorship: Sponsorship): List<Cms4PartnerDb> {
        return database
            .query<Cms4PartnerDb>(
                collectionName = "companies",
                "edition".whereEquals(year),
                "public".whereEquals(true),
                "logoUrl".whereNotEquals(null),
                "sponsoring".whereEquals(sponsorship.name)
            )
            .map {
                if (it.siteUrl == null) return@map it
                if (it.siteUrl.contains(Regex("^https?://"))) return@map it
                return@map it.copy(siteUrl = "https://${it.siteUrl}")
            }
    }

    suspend fun list(year: String): List<Cms4PartnerDb> {
        val collectionName = if (hasPartners(year)) {
            "companies-$year"
        } else {
            "companies"
        }
        return database
            .query<Cms4PartnerDb>(
                collectionName = collectionName,
                "edition".whereEquals(year),
                "public".whereEquals(true),
                "logoUrl".whereNotEquals(null)
            )
            .map {
                if (it.siteUrl == null) return@map it
                if (it.siteUrl.contains(Regex("^https?://"))) return@map it
                return@map it.copy(siteUrl = "https://${it.siteUrl}")
            }
    }

    suspend fun hasPartners(year: String): Boolean = database.count(collectionName = "companies-$year") > 0
}
