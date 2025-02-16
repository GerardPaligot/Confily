package com.paligot.confily.core.maps.entities

import com.paligot.confily.style.components.map.ui.models.MappingType
import com.paligot.confily.style.components.map.ui.models.OffsetUi
import com.paligot.confily.style.components.map.ui.models.PictogramType
import com.paligot.confily.style.components.map.ui.models.PictogramUi
import com.paligot.confily.style.components.map.ui.models.ShapeUi
import kotlin.native.ObjCName

@ObjCName("CoordinateEntity")
class Coordinate(val x: Float, val y: Float)

sealed class MapMarker(val order: Int, val name: String, val description: String?, val type: String) {
    @ObjCName("MapPictogramEntity")
    class MapPictogram(order: Int, name: String, description: String?, type: String, val position: Coordinate) :
        MapMarker(order, name, description, type)

    @ObjCName("MapShapeEntity")
    class MapShape(
        order: Int,
        name: String,
        description: String?,
        type: String,
        val start: Coordinate,
        val end: Coordinate
    ) : MapMarker(order, name, description, type)
}

fun MapMarker.MapShape.mapToMapShapeUi(): ShapeUi = ShapeUi(
    label = name,
    description = description,
    type = MappingType.valueOf(type),
    start = OffsetUi(x = start.x, y = start.y),
    end = OffsetUi(x = end.x, y = end.y)
)

fun MapMarker.MapPictogram.mapToMapPictogramUi(): PictogramUi = PictogramUi(
    label = name,
    description = description,
    type = PictogramType.valueOf(type),
    position = OffsetUi(x = position.x, y = position.y)
)
