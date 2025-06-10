package com.paligot.confily.backend.internals.infrastructure.firestore

import com.paligot.confily.models.MappingType
import com.paligot.confily.models.PictogramType

data class OffsetEntity(val x: Float = 0f, val y: Float = 0f)

data class MapShapeEntity(
    val order: Int = 0,
    val name: String = "",
    val description: String? = null,
    val start: OffsetEntity = OffsetEntity(),
    val end: OffsetEntity = OffsetEntity(),
    val type: MappingType = MappingType.Event
)

data class MapPictogramEntity(
    val order: Int = 0,
    val name: String = "",
    val description: String? = null,
    val position: OffsetEntity = OffsetEntity(),
    val type: PictogramType = PictogramType.Coffee
)

data class MapEntity(
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
    val shapes: List<MapShapeEntity> = emptyList(),
    val pictograms: List<MapPictogramEntity> = emptyList()
)
