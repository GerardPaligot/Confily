package com.paligot.confily.backend.menus.infrastructure.firestore

import com.paligot.confily.models.EventLunchMenu

fun LunchMenuEntity.convertToModel() = EventLunchMenu(
    name = name,
    dish = dish,
    accompaniment = accompaniment,
    dessert = dessert
)
