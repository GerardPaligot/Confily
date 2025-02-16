package com.paligot.confily.backend.map

import com.paligot.confily.models.MappingType
import com.paligot.confily.models.PictogramType

data class OffsetDb(val x: Float = 0f, val y: Float = 0f)

data class MapShapeDb(
    val order: Int = 0,
    val name: String = "",
    val description: String? = null,
    val start: OffsetDb = OffsetDb(),
    val end: OffsetDb = OffsetDb(),
    val type: MappingType = MappingType.Event
)

data class MapPictogramDb(
    val order: Int = 0,
    val name: String = "",
    val description: String? = null,
    val position: OffsetDb = OffsetDb(),
    val type: PictogramType = PictogramType.Coffee
)

data class MapDb(
    val id: String = "",
    val name: String = "",
    val description: String? = null,
    val color: String = "",
    val colorSelected: String = "",
    val filename: String = "",
    val order: Int = 0,
    val pictoSize: Int = 0,
    val url: String = "",
    val filledUrl: String? = null,
    val shapes: List<MapShapeDb> = emptyList(),
    val pictograms: List<MapPictogramDb> = emptyList()
)
