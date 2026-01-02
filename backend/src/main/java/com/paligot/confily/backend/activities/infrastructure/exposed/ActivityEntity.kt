package com.paligot.confily.backend.activities.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.partners.infrastructure.exposed.PartnerEntity
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import java.util.UUID

class ActivityEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ActivityEntity>(ActivitiesTable) {
        fun findByEvent(eventId: UUID): SizedIterable<ActivityEntity> = this
            .find { ActivitiesTable.eventId eq eventId }
    }

    var eventId by ActivitiesTable.eventId
    var event by EventEntity.Companion referencedOn ActivitiesTable.eventId
    var partnerId by ActivitiesTable.partnerId
    var partner by PartnerEntity.Companion referencedOn ActivitiesTable.partnerId
    var name by ActivitiesTable.name
    var startTime by ActivitiesTable.startTime
    var endTime by ActivitiesTable.endTime
    var createdAt by ActivitiesTable.createdAt
    var updatedAt by ActivitiesTable.updatedAt
}
