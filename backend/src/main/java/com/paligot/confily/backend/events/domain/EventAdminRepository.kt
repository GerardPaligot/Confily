package com.paligot.confily.backend.events.domain

import com.paligot.confily.models.inputs.CoCInput
import com.paligot.confily.models.inputs.EventInput
import com.paligot.confily.models.inputs.FeaturesActivatedInput

interface EventAdminRepository {
    suspend fun update(eventId: String, eventInput: EventInput): String
    suspend fun updateCoC(eventId: String, coc: CoCInput): String
    suspend fun updateFeatures(eventId: String, features: FeaturesActivatedInput): String
}
