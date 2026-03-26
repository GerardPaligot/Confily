package com.paligot.confily.backend.activities.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.integrations.domain.IntegrationProvider
import com.paligot.confily.backend.partners.infrastructure.exposed.PartnerEntity
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and
import java.util.UUID

class ActivityEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ActivityEntity>(ActivitiesTable) {
        fun findByEvent(eventId: UUID): SizedIterable<ActivityEntity> = this
            .find { ActivitiesTable.eventId eq eventId }

        fun findByExternalId(
            partnerId: UUID,
            externalId: String,
            provider: IntegrationProvider
        ): ActivityEntity? = this.find {
            val partnerOp = ActivitiesTable.partnerId eq partnerId
            val externalIdOp = ActivitiesTable.externalId eq externalId
            val providerOp = ActivitiesTable.externalProvider eq provider
            partnerOp and externalIdOp and providerOp
        }.firstOrNull()
    }

    var eventId by ActivitiesTable.eventId
    var event by EventEntity.Companion referencedOn ActivitiesTable.eventId
    var partnerId by ActivitiesTable.partnerId
    var partner by PartnerEntity.Companion referencedOn ActivitiesTable.partnerId
    var name by ActivitiesTable.name
    var startTime by ActivitiesTable.startTime
    var endTime by ActivitiesTable.endTime
    var externalId by ActivitiesTable.externalId
    var externalProvider by ActivitiesTable.externalProvider
    var createdAt by ActivitiesTable.createdAt
    var updatedAt by ActivitiesTable.updatedAt
}
