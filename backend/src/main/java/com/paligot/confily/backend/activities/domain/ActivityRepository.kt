package com.paligot.confily.backend.activities.domain

import com.paligot.confily.models.inputs.ActivityInput

interface ActivityRepository {
    suspend fun create(eventId: String, activity: ActivityInput): String
}
