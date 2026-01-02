package com.paligot.confily.backend.map.infrastructure.firestore

import com.paligot.confily.models.EventMap
import com.paligot.confily.models.MapPictogram
import com.paligot.confily.models.MapShape
import com.paligot.confily.models.Offset

fun MapEntity.convertToModel() = EventMap(
    id = id,
    name = name,
    color = color,
    colorSelected = colorSelected,
    order = order,
    pictoSize = pictoSize,
    url = url,
    filledUrl = filledUrl,
    shapes = shapes.map(MapShapeEntity::convertToModel),
    pictograms = pictograms.map(MapPictogramEntity::convertToModel)
)

fun MapShapeEntity.convertToModel() = MapShape(
    order = order,
    name = name,
    description = description,
    start = start.convertToModel(),
    end = end.convertToModel(),
    type = type
)

fun MapPictogramEntity.convertToModel() = MapPictogram(
    order = order,
    name = name,
    description = description,
    position = position.convertToModel(),
    type = type
)

fun OffsetEntity.convertToModel() = Offset(x = x, y = y)

fun MapShape.convertToEntity() = MapShapeEntity(
    order = order,
    name = name,
    description = description,
    start = start.convertToEntity(),
    end = end.convertToEntity(),
    type = type
)

fun MapPictogram.convertToEntity() = MapPictogramEntity(
    order = order,
    name = name,
    description = description,
    position = position.convertToEntity(),
    type = type
)

fun Offset.convertToEntity() = OffsetEntity(x = x, y = y)
