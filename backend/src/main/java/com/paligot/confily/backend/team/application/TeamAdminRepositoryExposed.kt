package com.paligot.confily.backend.team.application

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.internals.helpers.mimeType
import com.paligot.confily.backend.internals.helpers.slug
import com.paligot.confily.backend.internals.infrastructure.exposed.SocialEntity
import com.paligot.confily.backend.internals.infrastructure.exposed.SocialsTable
import com.paligot.confily.backend.internals.infrastructure.provider.CommonApi
import com.paligot.confily.backend.team.domain.TeamAdminRepository
import com.paligot.confily.backend.team.infrastructure.exposed.TeamEntity
import com.paligot.confily.backend.team.infrastructure.exposed.TeamGroupEntity
import com.paligot.confily.backend.team.infrastructure.exposed.TeamSocialsTable
import com.paligot.confily.backend.team.infrastructure.storage.TeamStorage
import com.paligot.confily.models.inputs.TeamMemberInput
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class TeamAdminRepositoryExposed(
    private val database: Database,
    private val commonApi: CommonApi,
    private val teamStorage: TeamStorage
) : TeamAdminRepository {

    override suspend fun create(eventId: String, teamInput: TeamMemberInput): String {
        val eventUuid = UUID.fromString(eventId)
        val teamUuid = UUID.randomUUID()
        val event = transaction(db = database) { EventEntity[eventUuid] }
        val url = teamInput.photoUrl?.let {
            val avatar = commonApi.fetchByteArray(teamInput.photoUrl!!)
            val bucketItem = teamStorage.saveTeamPicture(
                eventId = event.slug,
                id = teamInput.displayName.slug(),
                content = avatar,
                mimeType = teamInput.photoUrl!!.mimeType
            )
            bucketItem.url
        }
        return transaction(db = database) {
            // Find or create team group
            val teamGroup = TeamGroupEntity
                .findByName(eventUuid, teamInput.teamName)
                ?: throw NotFoundException("Team group not found: ${teamInput.teamName}")

            // Create team member
            val teamMember = TeamEntity.new(teamUuid) {
                this.event = event
                this.name = teamInput.displayName
                this.bio = teamInput.bio
                this.role = teamInput.role
                this.photoUrl = url
                this.teamGroup = teamGroup
                this.displayOrder = teamInput.order ?: 0
            }

            val teamMemberId = teamMember.id.value

            // Create socials
            if (teamInput.socials.isNotEmpty()) {
                val socialIds = teamInput.socials.map { socialInput ->
                    val socialEntity = SocialEntity.new {
                        this.event = event
                        this.platform = socialInput.type
                        this.url = socialInput.url
                    }
                    socialEntity.id.value
                }
                TeamSocialsTable.batchInsert(socialIds) { socialId ->
                    this[TeamSocialsTable.teamMemberId] = teamMemberId
                    this[TeamSocialsTable.socialId] = socialId
                }
            }

            teamMemberId.toString()
        }
    }

    override suspend fun update(eventId: String, teamMemberId: String, input: TeamMemberInput): String {
        val eventUuid = UUID.fromString(eventId)
        val teamUuid = UUID.fromString(teamMemberId)
        val event = transaction(db = database) { EventEntity[eventUuid] }
        val url = input.photoUrl?.let {
            val avatar = commonApi.fetchByteArray(input.photoUrl!!)
            val bucketItem = teamStorage.saveTeamPicture(
                eventId = event.slug,
                id = input.displayName.slug(),
                content = avatar,
                mimeType = input.photoUrl!!.mimeType
            )
            bucketItem.url
        }
        return transaction(db = database) {
            val teamGroup = TeamGroupEntity
                .findByName(eventUuid, input.teamName)
                ?: throw NotFoundException("Team group not found: ${input.teamName}")

            val teamMember = TeamEntity[teamUuid]

            // Update team member fields
            teamMember.name = input.displayName
            teamMember.bio = input.bio
            teamMember.role = input.role
            url?.let { teamMember.photoUrl = url }
            teamMember.teamGroup = teamGroup
            teamMember.displayOrder = input.order ?: 0

            // Delete old socials
            val socialIds = TeamSocialsTable.socialIds(teamMember.id.value)

            TeamSocialsTable.deleteWhere { TeamSocialsTable.teamMemberId eq teamUuid }
            if (socialIds.isNotEmpty()) {
                SocialsTable.deleteWhere { SocialsTable.id inList socialIds }
            }

            // Create new socials
            if (input.socials.isNotEmpty()) {
                val socialIds = input.socials.map { socialInput ->
                    val socialEntity = SocialEntity.new {
                        this.event = event
                        this.platform = socialInput.type
                        this.url = socialInput.url
                    }
                    socialEntity.id.value
                }
                TeamSocialsTable.batchInsert(socialIds) { socialId ->
                    this[TeamSocialsTable.teamMemberId] = teamUuid
                    this[TeamSocialsTable.socialId] = socialId
                }
            }

            teamMember.id.value.toString()
        }
    }
}
