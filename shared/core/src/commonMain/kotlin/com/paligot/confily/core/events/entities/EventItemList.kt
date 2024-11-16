package com.paligot.confily.core.events.entities

import com.paligot.confily.models.ui.EventItemListUi
import kotlinx.collections.immutable.toImmutableList
import kotlin.native.ObjCName

@ObjCName("EventItemListEntity")
class EventItemList(
    val future: List<EventItem>,
    val past: List<EventItem>
)

fun EventItemList.mapToUi(): EventItemListUi = EventItemListUi(
    future = future.map { it.mapToUi() }.toImmutableList(),
    past = past.map { it.mapToUi() }.toImmutableList()
)
