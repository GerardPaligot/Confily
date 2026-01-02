package com.paligot.confily.backend.third.parties.openplanner.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.internals.infrastructure.exposed.SocialEntity
import com.paligot.confily.backend.internals.infrastructure.exposed.SocialsTable
import com.paligot.confily.backend.speakers.infrastructure.exposed.SpeakerEntity
import com.paligot.confily.backend.speakers.infrastructure.exposed.SpeakerSocialsTable
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.SpeakerOP
import com.paligot.confily.models.SocialType
import com.paligot.confily.models.mapToSocialType
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteWhere

fun SpeakerOP.toEntity(event: EventEntity, photoUrl: String?): SpeakerEntity {
    val entity = SpeakerEntity
        .findByExternalId(event.id.value, this.id)
        ?.let { entity ->
            entity.name = this@toEntity.name
            entity.bio = this@toEntity.bio
            entity.photoUrl = photoUrl
            entity.email = this@toEntity.email
            entity.pronouns = this@toEntity.pronouns
            entity.company = this@toEntity.company
            entity.jobTitle = this@toEntity.jobTitle
            entity.updatedAt = Clock.System.now()
            entity
        }
        ?: SpeakerEntity.new {
            this.event = event
            this.name = this@toEntity.name
            this.bio = this@toEntity.bio
            this.photoUrl = photoUrl
            this.email = this@toEntity.email
            this.pronouns = this@toEntity.pronouns
            this.company = this@toEntity.company
            this.jobTitle = this@toEntity.jobTitle
            this.externalId = this@toEntity.id
        }

    val socialIds = SpeakerSocialsTable.socialIds(entity.id.value)

    SpeakerSocialsTable.deleteWhere { SpeakerSocialsTable.speakerId eq entity.id.value }
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
        SpeakerSocialsTable.batchInsert(socialIds) { socialId ->
            this[SpeakerSocialsTable.speakerId] = entity.id.value
            this[SpeakerSocialsTable.socialId] = socialId
        }
    }

    return entity
}
