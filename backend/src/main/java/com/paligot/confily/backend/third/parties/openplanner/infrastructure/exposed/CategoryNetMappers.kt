package com.paligot.confily.backend.third.parties.openplanner.infrastructure.exposed

import com.paligot.confily.backend.categories.infrastructure.exposed.CategoryEntity
import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.integrations.domain.IntegrationProvider
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.CategoryOP
import kotlinx.datetime.Clock

fun CategoryOP.toEntity(event: EventEntity, order: Int): CategoryEntity = CategoryEntity
    .findByExternalId(eventId = event.id.value, externalId = this.id, provider = IntegrationProvider.OPENPLANNER)
    ?.let { entity ->
        entity.name = this@toEntity.name
        entity.color = this@toEntity.color
        entity.updatedAt = Clock.System.now()
        entity
    }
    ?: CategoryEntity.new {
        this.event = event
        this.name = this@toEntity.name
        this.color = this@toEntity.color
        this.displayOrder = order
        this.externalId = this@toEntity.id
        this.externalProvider = IntegrationProvider.OPENPLANNER
    }
