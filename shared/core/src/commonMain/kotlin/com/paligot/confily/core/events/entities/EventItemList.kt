package com.paligot.confily.core.events.entities

import com.paligot.confily.events.ui.models.EventItemListUi
import kotlinx.collections.immutable.toImmutableList
import kotlin.native.ObjCName

@ObjCName("EventItemListEntity")
class EventItemList(
    val future: List<EventItem>,
    val past: List<EventItem>
)

fun EventItemList.mapToEventItemListUi(): EventItemListUi = EventItemListUi(
    future = future.map { it.mapToEventItemUi() }.toImmutableList(),
    past = past.map { it.mapToEventItemUi() }.toImmutableList()
)
