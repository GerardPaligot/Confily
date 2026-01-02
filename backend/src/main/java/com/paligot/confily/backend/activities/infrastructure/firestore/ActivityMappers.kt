package com.paligot.confily.backend.activities.infrastructure.firestore

import com.paligot.confily.models.Activity
import com.paligot.confily.models.inputs.ActivityInput

fun ActivityEntity.convertToModel() = Activity(
    id = id ?: "",
    name = name,
    startTime = startTime,
    endTime = endTime,
    partnerId = partnerId
)

fun ActivityInput.convertToEntity(id: String? = null) = ActivityEntity(
    id = id,
    name = name,
    startTime = startTime,
    endTime = endTime,
    partnerId = partnerId
)
