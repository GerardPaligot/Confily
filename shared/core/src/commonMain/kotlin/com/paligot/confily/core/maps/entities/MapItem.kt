package com.paligot.confily.core.maps.entities

import com.paligot.confily.infos.ui.models.MapItemUi
import kotlinx.collections.immutable.toImmutableList
import kotlin.native.ObjCName

@ObjCName("MapEntity")
class MapItem(
    val id: String,
    val name: String,
    val mapUrl: String,
    val colorHex: String,
    val selectedColorHex: String,
    val pictoSize: Int,
    val markers: List<MapMarker>
)

fun MapItem.mapToMapItemUi(): MapItemUi = MapItemUi(
    id = this.id,
    name = this.name,
    url = this.mapUrl,
    colorHex = this.colorHex,
    selectedColorHex = this.selectedColorHex,
    pictoSize = this.pictoSize,
    shapes = markers
        .filterIsInstance<MapMarker.MapShape>()
        .map(MapMarker.MapShape::mapToMapShapeUi)
        .toImmutableList(),
    pictograms = markers
        .filterIsInstance<MapMarker.MapPictogram>()
        .map(MapMarker.MapPictogram::mapToMapPictogramUi)
        .toImmutableList(),
    labels = markers
        .filter { it.name.isNotEmpty() }
        .sortedBy { it.order }
        .map { it.name }
        .distinct()
        .toImmutableList()
)
