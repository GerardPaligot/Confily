package com.paligot.confily.backend.export.application

import com.paligot.confily.backend.activities.infrastructure.exposed.ActivitiesTable
import com.paligot.confily.backend.activities.infrastructure.exposed.ActivityEntity
import com.paligot.confily.backend.activities.infrastructure.exposed.toModel
import com.paligot.confily.backend.export.domain.ExportRepository
import com.paligot.confily.backend.partners.infrastructure.exposed.PartnerEntity
import com.paligot.confily.backend.partners.infrastructure.exposed.SponsoringTypeEntity
import com.paligot.confily.backend.partners.infrastructure.exposed.SponsoringTypesTable
import com.paligot.confily.backend.partners.infrastructure.exposed.toModelV3
import com.paligot.confily.models.PartnersActivities
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class ExportPartnersRepositoryExposed(
    private val database: Database
) : ExportRepository<PartnersActivities> {
    override suspend fun get(eventId: String): PartnersActivities = transaction(db = database) {
        val eventUuid = UUID.fromString(eventId)
        PartnersActivities(
            types = SponsoringTypeEntity
                .findByEvent(eventUuid)
                .orderBy(SponsoringTypesTable.displayOrder to SortOrder.ASC)
                .map { it.typeName },
            partners = PartnerEntity
                .findByEvent(eventUuid)
                .map { it.toModelV3() },
            activities = ActivityEntity
                .findByEvent(eventUuid)
                .orderBy(ActivitiesTable.startTime to SortOrder.ASC)
                .map { it.toModel() }
        )
    }
}
