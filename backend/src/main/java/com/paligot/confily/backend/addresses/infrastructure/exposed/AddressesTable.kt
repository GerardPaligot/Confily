package com.paligot.confily.backend.addresses.infrastructure.exposed

import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object AddressesTable : UUIDTable("addresses") {
    val street = text("street")
    val postalCode = varchar("postal_code", 20)
    val city = varchar("city", 255)
    val country = varchar("country", 100)
    val countryCode = varchar("country_code", 3)
    val latitude = decimal("latitude", precision = 10, scale = 7).nullable()
    val longitude = decimal("longitude", precision = 10, scale = 7).nullable()
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Clock.System.now() }

    init {
        index(isUnique = false, city, country)
        index(isUnique = false, latitude, longitude)
    }
}
