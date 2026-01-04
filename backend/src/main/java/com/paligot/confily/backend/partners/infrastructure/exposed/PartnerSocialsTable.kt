package com.paligot.confily.backend.partners.infrastructure.exposed

import com.paligot.confily.backend.internals.infrastructure.exposed.SocialsTable
import com.paligot.confily.models.SocialItem
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

object PartnerSocialsTable : Table("partner_socials") {
    val partnerId = reference("partner_id", PartnersTable, onDelete = ReferenceOption.CASCADE)
    val socialId = reference("social_id", SocialsTable, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(partnerId, socialId)

    init {
        index(isUnique = false, partnerId)
        index(isUnique = false, socialId)
    }

    fun socialIds(partnerId: UUID): List<EntityID<UUID>> = this.selectAll()
        .where { PartnerSocialsTable.partnerId eq partnerId }
        .map { it[socialId] }

    fun socials(partnerId: UUID) = this
        .innerJoin(SocialsTable)
        .selectAll()
        .where { PartnerSocialsTable.partnerId eq partnerId }
        .map { row -> SocialItem(row[SocialsTable.platform], row[SocialsTable.url]) }
}
