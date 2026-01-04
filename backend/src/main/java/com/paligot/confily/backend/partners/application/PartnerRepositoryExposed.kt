package com.paligot.confily.backend.partners.application

import com.paligot.confily.backend.activities.infrastructure.exposed.ActivitiesTable
import com.paligot.confily.backend.activities.infrastructure.exposed.ActivityEntity
import com.paligot.confily.backend.activities.infrastructure.exposed.toModel
import com.paligot.confily.backend.partners.domain.PartnerRepository
import com.paligot.confily.backend.partners.infrastructure.exposed.PartnerEntity
import com.paligot.confily.backend.partners.infrastructure.exposed.PartnerSponsorshipsTable
import com.paligot.confily.backend.partners.infrastructure.exposed.SponsoringTypeEntity
import com.paligot.confily.backend.partners.infrastructure.exposed.SponsoringTypesTable
import com.paligot.confily.backend.partners.infrastructure.exposed.toModelV2
import com.paligot.confily.backend.partners.infrastructure.exposed.toModelV3
import com.paligot.confily.models.PartnerV2
import com.paligot.confily.models.PartnersActivities
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class PartnerRepositoryExposed(private val database: Database) : PartnerRepository {
    override fun list(eventId: String): Map<String, List<PartnerV2>> = transaction(db = database) {
        val eventUuid = UUID.fromString(eventId)
        SponsoringTypeEntity
            .findByEvent(eventUuid)
            .orderBy(SponsoringTypesTable.displayOrder to SortOrder.ASC)
            .associate { sponsoringType ->
                val partners = PartnerSponsorshipsTable
                    .partnerIds(sponsoringType.id.value)
                    .map { PartnerEntity[it].toModelV2() }
                sponsoringType.typeName to partners
            }
    }

    override suspend fun activities(eventId: String): PartnersActivities = transaction(db = database) {
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
