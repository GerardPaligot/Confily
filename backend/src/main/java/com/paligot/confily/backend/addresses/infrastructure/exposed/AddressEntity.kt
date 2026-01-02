package com.paligot.confily.backend.addresses.infrastructure.exposed

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class AddressEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<AddressEntity>(AddressesTable)

    var street by AddressesTable.street
    var postalCode by AddressesTable.postalCode
    var city by AddressesTable.city
    var country by AddressesTable.country
    var countryCode by AddressesTable.countryCode
    var latitude by AddressesTable.latitude
    var longitude by AddressesTable.longitude
    var createdAt by AddressesTable.createdAt
    var updatedAt by AddressesTable.updatedAt
}
