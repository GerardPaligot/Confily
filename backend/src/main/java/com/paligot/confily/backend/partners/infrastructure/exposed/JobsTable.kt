package com.paligot.confily.backend.partners.infrastructure.exposed

import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.date
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object JobsTable : UUIDTable("jobs") {
    val partnerId = reference("partner_id", PartnersTable, onDelete = ReferenceOption.CASCADE)
    val url = text("url")
    val title = varchar("title", 255)
    val location = varchar("location", 255).nullable()
    val salaryMin = integer("salary_min").nullable()
    val salaryMax = integer("salary_max").nullable()
    val salaryRecurrence = varchar("salary_recurrence", 50).nullable()
    val requirements = decimal("requirements", 3, 2).nullable()
    val propulsed = varchar("propulsed", 255).nullable()
    val publishDate = date("publish_date").nullable()
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Clock.System.now() }

    init {
        index(isUnique = false, partnerId)
        index(isUnique = false, location)
        index(isUnique = false, publishDate)
    }
}
