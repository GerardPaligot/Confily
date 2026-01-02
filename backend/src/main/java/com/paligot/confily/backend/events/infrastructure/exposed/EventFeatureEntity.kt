package com.paligot.confily.backend.events.infrastructure.exposed

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and
import java.util.UUID

class EventFeatureEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EventFeatureEntity>(EventFeaturesTable) {
        fun findByEvent(eventId: UUID): SizedIterable<EventFeatureEntity> = this
            .find { EventFeaturesTable.eventId eq eventId }

        fun findByFeatureKey(eventId: UUID, featureKey: FeatureKey): EventFeatureEntity? = this
            .find { (EventFeaturesTable.eventId eq eventId) and (EventFeaturesTable.featureKey eq featureKey) }
            .firstOrNull()
    }

    var eventId by EventFeaturesTable.eventId
    var event by EventEntity referencedOn EventFeaturesTable.eventId
    var featureKey by EventFeaturesTable.featureKey
    var enabled by EventFeaturesTable.enabled
    var createdAt by EventFeaturesTable.createdAt
    var updatedAt by EventFeaturesTable.updatedAt
}
