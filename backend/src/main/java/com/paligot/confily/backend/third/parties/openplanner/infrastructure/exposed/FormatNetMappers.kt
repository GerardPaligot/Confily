package com.paligot.confily.backend.third.parties.openplanner.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.formats.infrastructure.exposed.FormatEntity
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.FormatOP
import kotlinx.datetime.Clock

fun FormatOP.toEntity(event: EventEntity): FormatEntity = FormatEntity
    .findByExternalId(event.id.value, this.id)
    ?.let { entity ->
        entity.name = this@toEntity.name
        entity.updatedAt = Clock.System.now()
        entity
    }
    ?: FormatEntity.new {
        this.event = event
        this.name = this@toEntity.name
        this.externalId = this@toEntity.id
    }
