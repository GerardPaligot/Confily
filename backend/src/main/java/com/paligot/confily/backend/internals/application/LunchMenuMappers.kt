package com.paligot.confily.backend.internals.application

import com.paligot.confily.backend.internals.infrastructure.firestore.LunchMenuEntity
import com.paligot.confily.models.EventLunchMenu

fun LunchMenuEntity.convertToModel() = EventLunchMenu(
    name = name,
    dish = dish,
    accompaniment = accompaniment,
    dessert = dessert
)
