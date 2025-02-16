package com.paligot.confily.core.maps.entities

import com.paligot.confily.infos.ui.models.MapUi
import kotlinx.collections.immutable.toImmutableList
import kotlin.native.ObjCName

@ObjCName("MapItemListEntity")
class MapItemList(
    val items: List<MapItem>
)

fun MapItemList.mapToMapUi(): MapUi = MapUi(
    items = items.map(MapItem::mapToMapItemUi).toImmutableList()
)
