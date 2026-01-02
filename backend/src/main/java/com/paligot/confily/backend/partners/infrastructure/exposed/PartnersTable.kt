package com.paligot.confily.backend.partners.infrastructure.exposed

import com.paligot.confily.backend.addresses.infrastructure.exposed.AddressesTable
import com.paligot.confily.backend.events.infrastructure.exposed.EventsTable
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object PartnersTable : UUIDTable("partners") {
    val eventId = reference("event_id", EventsTable, onDelete = ReferenceOption.CASCADE)
    val name = varchar("name", 255)
    val description = text("description").nullable()
    val websiteUrl = text("website_url").nullable()
    val mediaSvg = text("media_svg").nullable()
    val mediaPng250 = text("media_png_250").nullable()
    val mediaPng500 = text("media_png_500").nullable()
    val mediaPng1000 = text("media_png_1000").nullable()
    val videoUrl = text("video_url").nullable()
    val addressId = reference("address_id", AddressesTable, onDelete = ReferenceOption.SET_NULL).nullable()
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Clock.System.now() }

    init {
        index(isUnique = false, eventId)
        index(isUnique = false, eventId, name)
    }
}
