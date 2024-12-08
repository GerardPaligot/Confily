package com.paligot.confily.core.events.entities

import com.paligot.confily.models.ui.MenuItemUi
import kotlin.native.ObjCName

@ObjCName("MenuItemEntity")
class MenuItem(
    val name: String,
    val dish: String,
    val accompaniment: String,
    val dessert: String
)

fun MenuItem.mapToMenuItemUi(): MenuItemUi = MenuItemUi(
    name = name,
    dish = dish,
    accompaniment = accompaniment,
    dessert = dessert
)
