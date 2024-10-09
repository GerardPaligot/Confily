package com.paligot.confily.wear.events.panes

import androidx.compose.runtime.Immutable
import com.paligot.confily.models.ui.MenuItemUi
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class EventsModelUi(val events: ImmutableList<EventItemModelUi>)

@Immutable
data class EventItemModelUi(val id: String, val name: String)

@Immutable
data class EventModelUi(val name: String)

@Immutable
data class MenusModelUi(val menus: ImmutableList<MenuItemUi>)
