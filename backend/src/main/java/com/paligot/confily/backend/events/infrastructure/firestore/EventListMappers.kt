package com.paligot.confily.backend.events.infrastructure.firestore

import com.paligot.confily.models.EventItemList

fun EventEntity.convertToEventItemList() = EventItemList(
    id = this.slugId,
    name = this.name,
    startDate = this.startDate,
    endDate = this.endDate
)
