package com.paligot.confily.backend.events.infrastructure.exposed

import com.paligot.confily.backend.addresses.infrastructure.exposed.AddressEntity
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

// Entity class for easier database access in repositories
class EventEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EventEntity>(EventsTable)

    var slug by EventsTable.slug
    var name by EventsTable.name
    var startDate by EventsTable.startDate
    var endDate by EventsTable.endDate
    var addressId by EventsTable.addressId
    var address by AddressEntity.Companion optionalReferencedOn EventsTable.addressId
    var defaultLanguage by EventsTable.defaultLanguage
    var contactEmail by EventsTable.contactEmail
    var contactPhone by EventsTable.contactPhone
    var coc by EventsTable.coc
    var cocUrl by EventsTable.cocUrl
    var faqUrl by EventsTable.faqUrl
    var publishedAt by EventsTable.publishedAt
    var createdAt by EventsTable.createdAt
    var updatedAt by EventsTable.updatedAt
}
