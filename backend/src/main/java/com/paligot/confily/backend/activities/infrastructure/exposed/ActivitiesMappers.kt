package com.paligot.confily.backend.activities.infrastructure.exposed

import com.paligot.confily.models.Activity
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun ActivityEntity.toModel(): Activity = Activity(
    id = this.id.value.toString(),
    name = this.name,
    startTime = this.startTime.toLocalDateTime(TimeZone.UTC).toString(),
    endTime = this.endTime?.toLocalDateTime(TimeZone.UTC)?.toString(),
    partnerId = this.partnerId.value.toString()
)
