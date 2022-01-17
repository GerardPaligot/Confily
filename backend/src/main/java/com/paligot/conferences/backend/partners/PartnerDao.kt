package com.paligot.conferences.backend.partners

import com.paligot.conferences.backend.database.Database
import com.paligot.conferences.backend.database.query
import com.paligot.conferences.backend.database.whereEquals
import com.paligot.conferences.backend.database.whereNotEquals

enum class Sponsorship { Gold, Silver, Bronze }

class PartnerDao(private val database: Database) {
    suspend fun listValidated(year: String, sponsorship: Sponsorship): List<PartnerDb> = database.query(
        "edition".whereEquals(year),
        "public".whereEquals(true),
        "logoUrl".whereNotEquals(null),
        "sponsoring".whereEquals(sponsorship.name)
    )
}