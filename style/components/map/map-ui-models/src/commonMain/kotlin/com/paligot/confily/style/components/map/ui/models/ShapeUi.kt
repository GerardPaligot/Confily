package com.paligot.confily.style.components.map.ui.models

data class ShapeUi(
    val label: String,
    val description: String?,
    val start: OffsetUi,
    val end: OffsetUi,
    val type: MappingType
)
