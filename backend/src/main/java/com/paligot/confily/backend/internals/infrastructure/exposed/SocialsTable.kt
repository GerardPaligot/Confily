package com.paligot.confily.backend.internals.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventsTable
import com.paligot.confily.models.SocialType
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object SocialsTable : UUIDTable("socials") {
    val eventId = reference("event_id", EventsTable, onDelete = ReferenceOption.CASCADE)
    val platform = enumeration<SocialType>("platform")
    val url = text("url")
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }

    init {
        index(isUnique = false, eventId)
    }
}
