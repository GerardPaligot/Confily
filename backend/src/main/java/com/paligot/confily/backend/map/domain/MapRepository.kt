package com.paligot.confily.backend.map.domain

import com.paligot.confily.models.EventMap

interface MapRepository {
    fun list(eventId: String): List<EventMap>
}
