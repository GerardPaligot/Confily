package com.paligot.confily.backend.events.infrastructure.exposed

import com.paligot.confily.models.EventItemList

fun EventEntity.toEventItemList(): EventItemList = EventItemList(
    id = this.id.value.toString(),
    name = this.name,
    startDate = this.startDate.toString(),
    endDate = this.endDate.toString()
)
