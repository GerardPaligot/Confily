package com.paligot.confily.backend.sessions.infrastructure.exposed

import com.paligot.confily.backend.addresses.infrastructure.exposed.toModel
import com.paligot.confily.models.Session

fun EventSessionEntity.toModel(): Session.Event = Session.Event(
    id = this.id.value.toString(),
    title = this.title,
    description = this.description ?: "",
    address = address?.toModel()
)
