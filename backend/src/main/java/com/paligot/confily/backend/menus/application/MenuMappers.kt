package com.paligot.confily.backend.menus.application

import com.paligot.confily.backend.internals.infrastructure.firestore.LunchMenuEntity
import com.paligot.confily.models.inputs.LunchMenuInput

internal fun LunchMenuInput.convertToEntity(): LunchMenuEntity = LunchMenuEntity(
    name = name,
    dish = dish,
    accompaniment = accompaniment,
    dessert = dessert
)
