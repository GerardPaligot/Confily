package org.gdglille.devfest.backend.partners

import org.gdglille.devfest.backend.database.Database
import org.gdglille.devfest.backend.database.query
import org.gdglille.devfest.backend.database.whereEquals
import org.gdglille.devfest.backend.database.whereNotEquals

enum class Sponsorship { Gold, Silver, Bronze }

class PartnerDao(private val database: Database) {
    suspend fun listValidated(year: String, sponsorship: Sponsorship): List<PartnerDb> = database.query(
        "edition".whereEquals(year),
        "public".whereEquals(true),
        "logoUrl".whereNotEquals(null),
        "sponsoring".whereEquals(sponsorship.name)
    )
}