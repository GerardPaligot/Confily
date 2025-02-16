package com.paligot.confily.backend.map

import com.paligot.confily.models.EventMap
import com.paligot.confily.models.MapPictogram
import com.paligot.confily.models.MapShape
import com.paligot.confily.models.Offset

fun MapDb.convertToModel() = EventMap(
    id = id,
    name = name,
    color = color,
    colorSelected = colorSelected,
    order = order,
    pictoSize = pictoSize,
    url = url,
    filledUrl = filledUrl,
    shapes = shapes.map(MapShapeDb::convertToModel),
    pictograms = pictograms.map(MapPictogramDb::convertToModel)
)

fun MapShapeDb.convertToModel() = MapShape(
    order = order,
    name = name,
    description = description,
    start = start.convertToModel(),
    end = end.convertToModel(),
    type = type
)

fun MapPictogramDb.convertToModel() = MapPictogram(
    order = order,
    name = name,
    description = description,
    position = position.convertToModel(),
    type = type
)

fun OffsetDb.convertToModel() = Offset(x = x, y = y)

fun MapShape.convertToDb() = MapShapeDb(
    order = order,
    name = name,
    description = description,
    start = start.convertToDb(),
    end = end.convertToDb(),
    type = type
)

fun MapPictogram.convertToDb() = MapPictogramDb(
    order = order,
    name = name,
    description = description,
    position = position.convertToDb(),
    type = type
)

fun Offset.convertToDb() = OffsetDb(x = x, y = y)
