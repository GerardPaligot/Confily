package com.paligot.confily.backend.events.infrastructure.exposed

import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object EventFeaturesTable : UUIDTable("event_features") {
    val eventId = reference("event_id", EventsTable).index()
    val featureKey = enumeration<FeatureKey>("feature_key")
    val enabled = bool("enabled").default(false)
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Clock.System.now() }

    init {
        uniqueIndex(eventId, featureKey)
        index(isUnique = false, eventId, enabled)
    }
}

enum class FeatureKey(val key: String) {
    Networking("has_networking")
}
