package com.paligot.confily.backend.map.infrastructure.exposed

import com.paligot.confily.models.EventMap
import com.paligot.confily.models.MapPictogram
import com.paligot.confily.models.MapShape
import com.paligot.confily.models.Offset

fun MapEntity.toModel(shapes: List<MapShape>, pictograms: List<MapPictogram>): EventMap =
    EventMap(
        id = this.id.value.toString(),
        name = this.name,
        color = this.color ?: "",
        colorSelected = this.colorSelected ?: "",
        order = this.displayOrder,
        url = this.url,
        filledUrl = this.filledUrl,
        pictoSize = this.pictoSize,
        shapes = shapes,
        pictograms = pictograms
    )

fun MapShapeEntity.toModel(): MapShape = MapShape(
    order = this.displayOrder,
    name = this.name,
    description = this.description,
    start = Offset(
        x = this.startX.toFloat(),
        y = this.startY.toFloat()
    ),
    end = Offset(
        x = this.endX.toFloat(),
        y = this.endY.toFloat()
    ),
    type = this.type
)

fun MapPictogramEntity.toModel(): MapPictogram = MapPictogram(
    order = this.displayOrder,
    name = this.name,
    description = this.description,
    position = Offset(
        x = this.positionX.toFloat(),
        y = this.positionY.toFloat()
    ),
    type = this.type
)
