package com.paligot.confily.backend.menus.infrastructure.exposed

import com.paligot.confily.models.EventLunchMenu

fun LunchMenuEntity.toModel(): EventLunchMenu = EventLunchMenu(
    name = this.name,
    dish = this.dish,
    accompaniment = this.accompaniment,
    dessert = this.dessert
)
