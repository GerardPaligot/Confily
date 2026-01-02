package com.paligot.confily.backend.third.parties.openplanner.infrastructure.exposed

import com.paligot.confily.backend.NotAcceptableException
import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.integrations.domain.IntegrationProvider
import com.paligot.confily.backend.internals.infrastructure.exposed.SocialEntity
import com.paligot.confily.backend.internals.infrastructure.exposed.SocialsTable
import com.paligot.confily.backend.team.infrastructure.exposed.TeamEntity
import com.paligot.confily.backend.team.infrastructure.exposed.TeamGroupEntity
import com.paligot.confily.backend.team.infrastructure.exposed.TeamSocialsTable
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.TeamOP
import com.paligot.confily.models.SocialType
import com.paligot.confily.models.mapToSocialType
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteWhere

fun List<TeamOP>.groups(event: EventEntity): List<TeamGroupEntity> = this
    .mapNotNull { it.team }.toSet()
    .mapIndexed { index, groupName ->
        TeamGroupEntity
            .findByName(event.id.value, groupName)
            ?.let { entity ->
                entity.name = groupName
                entity
            }
            ?: TeamGroupEntity.new {
                this.event = event
                this.name = groupName
                this.displayOrder = index
            }
    }

fun TeamOP.toEntity(event: EventEntity, order: Int, photoUrl: String?): TeamEntity {
    if (this.team == null) {
        throw NotAcceptableException("Team member ${this.name} must have a team group")
    }
    val teamGroup = TeamGroupEntity
        .findByName(eventId = event.id.value, name = this.team)
        ?: throw NotFoundException("Team group ${this.team} not found for event ${event.id.value}")

    val entity = TeamEntity
        .findByExternalId(eventId = event.id.value, externalId = this.id, provider = IntegrationProvider.OPENPLANNER)
        ?.let { entity ->
            entity.event = event
            entity.name = this@toEntity.name
            entity.role = this@toEntity.role
            entity.bio = this@toEntity.bio ?: ""
            entity.photoUrl = photoUrl
            entity.displayOrder = order
            entity.teamGroup = teamGroup
            entity.updatedAt = Clock.System.now()
            entity
        }
        ?: TeamEntity.new {
            this.event = event
            this.name = this@toEntity.name
            this.role = this@toEntity.role
            this.bio = this@toEntity.bio ?: ""
            this.photoUrl = photoUrl
            this.displayOrder = order
            this.teamGroup = teamGroup
            this.externalId = this@toEntity.id
            this.externalProvider = IntegrationProvider.OPENPLANNER
        }
    val socialIds = TeamSocialsTable.socialIds(entity.id.value)
    TeamSocialsTable.deleteWhere { TeamSocialsTable.teamMemberId eq entity.id.value }
    if (socialIds.isNotEmpty()) {
        SocialsTable.deleteWhere { SocialsTable.id inList socialIds }
    }

    val socialTypes = SocialType.entries.map { it.name.lowercase() }
    if (socials.isNotEmpty()) {
        val socialIds = socials
            .filter { socialTypes.contains(it.name.lowercase()) }
            .map { socialInput ->
                val socialEntity = SocialEntity.new {
                    this.event = event
                    this.platform = socialInput.name.mapToSocialType()
                    this.url = socialInput.link
                }
                socialEntity.id.value
            }
        TeamSocialsTable.batchInsert(socialIds) { socialId ->
            this[TeamSocialsTable.teamMemberId] = entity.id.value
            this[TeamSocialsTable.socialId] = socialId
        }
    }
    return entity
}
