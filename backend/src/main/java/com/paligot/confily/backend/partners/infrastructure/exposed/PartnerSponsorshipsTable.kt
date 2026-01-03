package com.paligot.confily.backend.partners.infrastructure.exposed

import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

object PartnerSponsorshipsTable : Table("partner_sponsorships") {
    val partnerId = reference("partner_id", PartnersTable, onDelete = ReferenceOption.CASCADE)
    val sponsoringTypeId = reference(
        "sponsoring_type_id",
        SponsoringTypesTable,
        onDelete = ReferenceOption.CASCADE
    )
    val displayOrder = integer("display_order").default(0)
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }

    override val primaryKey = PrimaryKey(partnerId, sponsoringTypeId)

    init {
        index(isUnique = false, partnerId)
        index(isUnique = false, sponsoringTypeId)
    }

    fun partnerIds(sponsoringTypeId: UUID): List<UUID> = this
        .innerJoin(PartnersTable)
        .selectAll()
        .where { PartnerSponsorshipsTable.sponsoringTypeId eq sponsoringTypeId }
        .orderBy(displayOrder to SortOrder.ASC)
        .map { it[partnerId].value }

    fun partnerExist(partnerId: UUID, sponsoringTypeId: UUID): Boolean = this
        .selectAll()
        .where {
            val partnerOp = PartnerSponsorshipsTable.partnerId eq partnerId
            val sponsoringTypeOp = PartnerSponsorshipsTable.sponsoringTypeId eq sponsoringTypeId
            partnerOp and sponsoringTypeOp
        }
        .count() > 0
}
