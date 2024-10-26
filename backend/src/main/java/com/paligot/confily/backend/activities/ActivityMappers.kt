package com.paligot.confily.backend.activities

import com.paligot.confily.models.Activity
import com.paligot.confily.models.inputs.ActivityInput

fun ActivityDb.convertToModel() = Activity(
    id = id ?: "",
    name = name,
    startTime = startTime,
    endTime = endTime,
    partnerId = partnerId
)

fun ActivityInput.convertToDb(id: String? = null) = ActivityDb(
    id = id,
    name = name,
    startTime = startTime,
    endTime = endTime,
    partnerId = partnerId
)
