package com.paligot.confily.backend.third.parties.openplanner.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.integrations.domain.IntegrationProvider
import com.paligot.confily.backend.sessions.infrastructure.exposed.EventSessionTrackEntity
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.TrackOP

fun TrackOP.toEventSessionTrackEntity(order: Int, event: EventEntity) = EventSessionTrackEntity
    .findByExternalId(eventId = event.id.value, externalId = this.id, provider = IntegrationProvider.OPENPLANNER)
    ?.let { entity ->
        entity.trackName = this@toEventSessionTrackEntity.name
        entity.displayOrder = order
        entity
    }
    ?: EventSessionTrackEntity.new {
        this.event = event
        this.trackName = this@toEventSessionTrackEntity.name
        this.displayOrder = order
        this.externalId = this@toEventSessionTrackEntity.id
        this.externalProvider = IntegrationProvider.OPENPLANNER
    }
