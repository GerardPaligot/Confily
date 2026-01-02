package com.paligot.confily.backend.activities.infrastructure.exposed

import com.paligot.confily.models.Activity

fun ActivityEntity.toModel(): Activity = Activity(
    id = this.id.value.toString(),
    name = this.name,
    startTime = this.startTime.toString(),
    endTime = this.endTime?.toString(),
    partnerId = this.partnerId.value.toString()
)
