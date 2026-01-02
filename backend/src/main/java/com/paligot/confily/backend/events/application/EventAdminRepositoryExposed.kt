package com.paligot.confily.backend.events.application

import com.paligot.confily.backend.addresses.infrastructure.exposed.toEntity
import com.paligot.confily.backend.addresses.infrastructure.provider.GeocodeApi
import com.paligot.confily.backend.events.domain.EventAdminRepository
import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.events.infrastructure.exposed.EventFeatureEntity
import com.paligot.confily.backend.events.infrastructure.exposed.EventSocialsTable
import com.paligot.confily.backend.events.infrastructure.exposed.FeatureKey
import com.paligot.confily.backend.internals.infrastructure.exposed.SocialEntity
import com.paligot.confily.backend.internals.infrastructure.exposed.SocialsTable
import com.paligot.confily.backend.partners.infrastructure.exposed.SponsoringTypeEntity
import com.paligot.confily.backend.partners.infrastructure.exposed.SponsoringTypesTable
import com.paligot.confily.backend.sessions.infrastructure.exposed.EventSessionTrackEntity
import com.paligot.confily.backend.sessions.infrastructure.exposed.EventSessionTracksTable
import com.paligot.confily.backend.team.infrastructure.exposed.TeamGroupEntity
import com.paligot.confily.backend.team.infrastructure.exposed.TeamGroupsTable
import com.paligot.confily.models.inputs.CoCInput
import com.paligot.confily.models.inputs.EventInput
import com.paligot.confily.models.inputs.FeaturesActivatedInput
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class EventAdminRepositoryExposed(
    private val database: Database,
    private val geocodeApi: GeocodeApi
) : EventAdminRepository {
    override suspend fun update(eventId: String, eventInput: EventInput): String {
        val geocode = eventInput.address?.let { addressInput ->
            geocodeApi.geocode(addressInput)
        }
        return transaction(db = database) {
            val event = EventEntity[UUID.fromString(eventId)]

            // Update event fields
            eventInput.name?.let { event.name = it }
            eventInput.contactEmail?.let { event.contactEmail = it }
            event.contactPhone = eventInput.contactPhone
            eventInput.faqLink?.let { event.faqUrl = it }
            event.updatedAt = Clock.System.now()
            if (eventInput.published && event.publishedAt != null) {
                event.publishedAt = Clock.System.now()
            }

            // Update address if provided
            if (geocode != null && geocode.results.isNotEmpty()) {
                event.address = geocode.toEntity()
            }

            // Update sponsoring types if provided
            if (eventInput.sponsoringTypes != null && eventInput.sponsoringTypes!!.isNotEmpty()) {
                SponsoringTypesTable.deleteWhere { SponsoringTypesTable.eventId eq event.id }
                eventInput.sponsoringTypes!!.forEachIndexed { index, sponsoringTypeInput ->
                    SponsoringTypeEntity.new {
                        this.event = event
                        typeName = sponsoringTypeInput
                        displayOrder = index
                    }
                }
            }

            // Update team groups if provided
            if (eventInput.teamGroups != null && eventInput.teamGroups!!.isNotEmpty()) {
                TeamGroupsTable.deleteWhere { TeamGroupsTable.eventId eq event.id }
                eventInput.teamGroups!!.forEach { teamGroupInput ->
                    TeamGroupEntity.new {
                        this.event = event
                        name = teamGroupInput.name
                        displayOrder = teamGroupInput.order
                    }
                }
            }

            // Update socials if provided
            if (eventInput.socials != null && eventInput.socials!!.isNotEmpty()) {
                val socialIds = EventSocialsTable.socialIds(event.id.value)

                EventSocialsTable.deleteWhere { EventSocialsTable.eventId eq event.id.value }
                if (socialIds.isNotEmpty()) {
                    SocialsTable.deleteWhere { SocialsTable.id inList socialIds }
                }

                val newSocialIds = eventInput.socials!!.map { socialInput ->
                    SocialEntity.new {
                        this.event = event
                        platform = socialInput.type
                        url = socialInput.url
                    }.id.value
                }
                EventSocialsTable.batchInsert(newSocialIds) { socialId ->
                    this[EventSocialsTable.eventId] = event.id.value
                    this[EventSocialsTable.socialId] = socialId
                }
            }

            // Update event session tracks if provided
            eventInput.eventSessionTracks?.let { tracks ->
                EventSessionTracksTable.deleteWhere { EventSessionTracksTable.eventId eq event.id }
                tracks.forEachIndexed { index, trackName ->
                    EventSessionTrackEntity.new {
                        this.event = event
                        this.trackName = trackName
                        displayOrder = index
                    }
                }
            }

            eventId
        }
    }

    override suspend fun updateCoC(eventId: String, coc: CoCInput): String = transaction(db = database) {
        val event = EventEntity[UUID.fromString(eventId)]
        event.coc = coc.coc
        event.updatedAt = Clock.System.now()
        eventId
    }

    override suspend fun updateFeatures(
        eventId: String,
        features: FeaturesActivatedInput
    ): String = transaction(db = database) {
        val event = EventEntity[UUID.fromString(eventId)]
        // Find existing feature
        val existingFeature = EventFeatureEntity.findByFeatureKey(event.id.value, FeatureKey.Networking)
        if (existingFeature != null) {
            // Update existing feature
            existingFeature.enabled = features.hasNetworking
            existingFeature.updatedAt = Clock.System.now()
        } else {
            // Create new feature
            EventFeatureEntity.new {
                this.event = event
                featureKey = FeatureKey.Networking
                enabled = features.hasNetworking
            }
        }
        eventId
    }
}
