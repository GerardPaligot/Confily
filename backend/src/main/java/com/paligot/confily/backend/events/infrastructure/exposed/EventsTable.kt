package com.paligot.confily.backend.events.infrastructure.exposed

import com.paligot.confily.backend.addresses.infrastructure.exposed.AddressesTable
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.date
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

// Table definition for SQL schema creation
object EventsTable : UUIDTable("events") {
    val slug = varchar("slug", 100).uniqueIndex()
    val name = varchar("name", 255)
    val startDate = date("start_date")
    val endDate = date("end_date")
    val addressId = reference("address_id", AddressesTable).nullable()
    val defaultLanguage = varchar("default_language", 10).default("en")
    val contactEmail = varchar("contact_email", 255).nullable()
    val contactPhone = varchar("contact_phone", 50).nullable()
    val coc = text("coc").nullable()
    val cocUrl = text("coc_url").nullable()
    val faqUrl = text("faq_url").nullable()
    val publishedAt = timestamp("published_at").nullable()
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Clock.System.now() }

    init {
        // T099: Performance index for event listing by date
        index(isUnique = false, startDate)
    }
}
