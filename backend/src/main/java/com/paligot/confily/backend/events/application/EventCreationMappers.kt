package com.paligot.confily.backend.events.application

import com.paligot.confily.backend.internals.helpers.slug
import com.paligot.confily.backend.internals.infrastructure.firestore.AddressEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.EventEntity
import com.paligot.confily.models.inputs.CreatingEventInput
import java.util.*

internal fun CreatingEventInput.convertToEntity(addressDb: AddressEntity, language: String): EventEntity = EventEntity(
    slugId = name.slug(),
    year = year,
    apiKey = UUID.randomUUID().toString(),
    name = this.name,
    defaultLanguage = language,
    address = addressDb,
    startDate = this.startDate,
    endDate = this.endDate,
    contactPhone = this.contactPhone,
    contactEmail = this.contactEmail,
    updatedAt = this.updatedAt
)
