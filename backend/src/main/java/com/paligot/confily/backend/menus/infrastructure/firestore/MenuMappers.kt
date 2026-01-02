package com.paligot.confily.backend.menus.infrastructure.firestore

import com.paligot.confily.models.inputs.LunchMenuInput

internal fun LunchMenuInput.convertToEntity(): LunchMenuEntity = LunchMenuEntity(
    name = name,
    dish = dish,
    accompaniment = accompaniment,
    dessert = dessert
)
