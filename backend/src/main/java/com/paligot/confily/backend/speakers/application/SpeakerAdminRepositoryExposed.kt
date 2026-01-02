package com.paligot.confily.backend.speakers.application

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.internals.helpers.mimeType
import com.paligot.confily.backend.internals.helpers.slug
import com.paligot.confily.backend.internals.infrastructure.exposed.SocialEntity
import com.paligot.confily.backend.internals.infrastructure.exposed.SocialsTable
import com.paligot.confily.backend.internals.infrastructure.provider.CommonApi
import com.paligot.confily.backend.speakers.domain.SpeakerAdminRepository
import com.paligot.confily.backend.speakers.infrastructure.exposed.SpeakerEntity
import com.paligot.confily.backend.speakers.infrastructure.exposed.SpeakerSocialsTable
import com.paligot.confily.backend.speakers.infrastructure.storage.SpeakerStorage
import com.paligot.confily.models.inputs.SpeakerInput
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class SpeakerAdminRepositoryExposed(
    private val database: Database,
    private val commonApi: CommonApi,
    private val speakerStorage: SpeakerStorage
) : SpeakerAdminRepository {
    override suspend fun create(eventId: String, speakerInput: SpeakerInput): String {
        val eventUuid = UUID.fromString(eventId)
        val speakerUuid = UUID.randomUUID()
        val event = transaction(db = database) { EventEntity[eventUuid] }
        val avatar = commonApi.fetchByteArray(speakerInput.photoUrl)
        val bucketItem = speakerStorage.saveProfile(
            eventId = event.slug,
            id = speakerInput.displayName.slug(),
            content = avatar,
            mimeType = speakerInput.photoUrl.mimeType
        )
        transaction(db = database) {
            SpeakerEntity.new(speakerUuid) {
                this.event = event
                this.name = speakerInput.displayName
                this.pronouns = speakerInput.pronouns
                this.bio = speakerInput.bio
                this.photoUrl = speakerInput.photoUrl
                this.email = speakerInput.email
                this.company = speakerInput.company
                this.jobTitle = speakerInput.jobTitle
                this.photoUrl = bucketItem.url
            }

            if (speakerInput.socials.isNotEmpty()) {
                val socialIds = speakerInput.socials.map { social ->
                    SocialEntity.new {
                        this.event = event
                        this.platform = social.type
                        this.url = social.url
                    }.id.value
                }
                SpeakerSocialsTable.batchInsert(socialIds) { socialId ->
                    this[SpeakerSocialsTable.speakerId] = speakerUuid
                    this[SpeakerSocialsTable.socialId] = socialId
                }
            }
        }
        return speakerUuid.toString()
    }

    override suspend fun update(eventId: String, speakerId: String, input: SpeakerInput): String {
        val eventUuid = UUID.fromString(eventId)
        val speakerUuid = UUID.fromString(speakerId)
        val event = transaction(db = database) { EventEntity[eventUuid] }
        val avatar = commonApi.fetchByteArray(input.photoUrl)
        val bucketItem = speakerStorage.saveProfile(
            eventId = event.slug,
            id = input.displayName.slug(),
            content = avatar,
            mimeType = input.photoUrl.mimeType
        )
        return transaction(db = database) {
            val speaker = SpeakerEntity
                .findById(eventUuid, speakerUuid)
                ?: throw NotFoundException("Speaker not found: $speakerId")

            speaker.name = input.displayName
            speaker.pronouns = input.pronouns
            speaker.bio = input.bio
            speaker.photoUrl = input.photoUrl
            speaker.email = input.email
            speaker.company = input.company
            speaker.jobTitle = input.jobTitle
            speaker.photoUrl = bucketItem.url

            // Delete old socials
            val socialIds = SpeakerSocialsTable.socialIds(speaker.id.value)

            SpeakerSocialsTable.deleteWhere { SpeakerSocialsTable.speakerId eq speakerUuid }
            if (socialIds.isNotEmpty()) {
                SocialsTable.deleteWhere { SocialsTable.id inList socialIds }
            }

            if (input.socials.isNotEmpty()) {
                val socialIds = input.socials.map { social ->
                    SocialEntity.new {
                        this.event = event
                        this.platform = social.type
                        this.url = social.url
                    }.id.value
                }
                SpeakerSocialsTable.batchInsert(socialIds) { socialId ->
                    this[SpeakerSocialsTable.speakerId] = speakerUuid
                    this[SpeakerSocialsTable.socialId] = socialId
                }
            }

            speaker.id.value.toString()
        }
    }
}
