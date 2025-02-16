package com.paligot.confily.core.maps

import com.paligot.confily.core.maps.entities.Coordinate
import com.paligot.confily.core.maps.entities.MapFilledItem
import com.paligot.confily.core.maps.entities.MapItem
import com.paligot.confily.core.maps.entities.MapMarker
import com.paligot.confily.db.SelectFilledMaps
import com.paligot.confily.db.SelectMaps
import com.paligot.confily.db.SelectPictograms
import com.paligot.confily.db.SelectShapes

fun SelectMaps.mapToEntity(
    shapes: List<SelectShapes>,
    pictograms: List<SelectPictograms>
): MapItem {
    val markers = (
        shapes.filter { it.map_id == id }.map(SelectShapes::mapToEntity) +
            pictograms.filter { it.map_id == id }.map(SelectPictograms::mapToEntity)
        )
    return MapItem(
        id = id,
        name = name,
        mapUrl = url,
        colorHex = color,
        selectedColorHex = selected_color,
        pictoSize = picto_size.toInt(),
        markers = markers.sortedBy { it.order }
    )
}

fun SelectFilledMaps.mapToEntity(): MapFilledItem = MapFilledItem(
    name = name,
    url = filled_url
)

fun SelectShapes.mapToEntity(): MapMarker.MapShape = MapMarker.MapShape(
    order = order_.toInt(),
    name = name,
    description = description,
    type = type,
    start = Coordinate(x = start_x.toFloat(), y = start_y.toFloat()),
    end = Coordinate(x = end_x.toFloat(), y = end_y.toFloat())
)

fun SelectPictograms.mapToEntity(): MapMarker.MapPictogram = MapMarker.MapPictogram(
    order = order_.toInt(),
    name = name,
    description = description,
    type = type,
    position = Coordinate(x = x.toFloat(), y = y.toFloat())
)
