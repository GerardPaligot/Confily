package com.paligot.confily.backend.events.application

import com.paligot.confily.backend.internals.infrastructure.firestore.EventEntity
import com.paligot.confily.models.EventItemList

fun EventEntity.convertToEventItemList() = EventItemList(
    id = this.slugId,
    name = this.name,
    startDate = this.startDate,
    endDate = this.endDate
)
