package com.paligot.confily.backend.events.domain

import com.paligot.confily.models.CreatedEvent
import com.paligot.confily.models.Event
import com.paligot.confily.models.EventList
import com.paligot.confily.models.EventV2
import com.paligot.confily.models.EventV3
import com.paligot.confily.models.EventV4
import com.paligot.confily.models.inputs.CreatingEventInput

interface EventRepository {
    suspend fun list(): EventList
    suspend fun getWithPartners(eventId: String): Event
    suspend fun getV2(eventId: String): EventV2
    suspend fun getV3(eventId: String): EventV3
    suspend fun getV4(eventId: String): EventV4
    suspend fun create(eventInput: CreatingEventInput, language: String): CreatedEvent
}
