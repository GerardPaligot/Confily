package org.gdglille.devfest.backend.partners.cms4partners

import com.google.cloud.firestore.Firestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.gdglille.devfest.backend.database.Database
import org.gdglille.devfest.backend.database.DatabaseType
import org.gdglille.devfest.backend.database.query
import org.gdglille.devfest.backend.database.whereEquals
import org.gdglille.devfest.backend.database.whereNotEquals

enum class Sponsorship { Gold, Silver, Bronze, Other }

class Cms4PartnersDao(
    private val firestore: Firestore,
    private val projectName: String,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend fun list(year: String, sponsorship: Sponsorship): List<Cms4PartnerDb> {
        val database = Database.Factory.create(
            DatabaseType.FirestoreDb(
                firestore = firestore,
                projectName = projectName,
                collectionName = "companies",
                dispatcher = dispatcher
            )
        )
        return database
            .query<Cms4PartnerDb>(
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
        val database = Database.Factory.create(
            DatabaseType.FirestoreDb(
                firestore = firestore,
                projectName = projectName,
                collectionName = "companies-$year",
                dispatcher = dispatcher
            )
        )
        return database
            .query<Cms4PartnerDb>(
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

    suspend fun hasPartners(year: String): Boolean {
        val database = Database.Factory.create(
            DatabaseType.FirestoreDb(
                firestore = firestore,
                projectName = projectName,
                collectionName = "companies-$year",
                dispatcher = dispatcher
            )
        )
        return database.count() > 0
    }
}
