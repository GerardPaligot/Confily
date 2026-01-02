package com.paligot.confily.backend.partners.infrastructure.exposed

import com.paligot.confily.backend.addresses.infrastructure.exposed.AddressEntity
import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and
import java.util.UUID

class PartnerEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<PartnerEntity>(PartnersTable) {
        fun findByEvent(eventId: UUID): SizedIterable<PartnerEntity> = this
            .find { PartnersTable.eventId eq eventId }

        fun findById(eventId: UUID, partnerId: UUID): PartnerEntity? = this
            .find { (PartnersTable.eventId eq eventId) and (PartnersTable.id eq partnerId) }
            .firstOrNull()
    }

    var eventId by PartnersTable.eventId
    var event by EventEntity.Companion referencedOn PartnersTable.eventId
    var name by PartnersTable.name
    var description by PartnersTable.description
    var websiteUrl by PartnersTable.websiteUrl
    var mediaSvg by PartnersTable.mediaSvg
    var mediaPng250 by PartnersTable.mediaPng250
    var mediaPng500 by PartnersTable.mediaPng500
    var mediaPng1000 by PartnersTable.mediaPng1000
    var videoUrl by PartnersTable.videoUrl
    var addressId by PartnersTable.addressId
    var address by AddressEntity.Companion optionalReferencedOn PartnersTable.addressId
    var createdAt by PartnersTable.createdAt
    var updatedAt by PartnersTable.updatedAt
}
