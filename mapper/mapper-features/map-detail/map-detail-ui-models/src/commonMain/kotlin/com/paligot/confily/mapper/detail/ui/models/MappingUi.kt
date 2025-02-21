package com.paligot.confily.mapper.detail.ui.models

import com.paligot.confily.style.components.map.ui.models.OffsetUi
import com.paligot.confily.style.components.map.ui.models.PictogramUi
import com.paligot.confily.style.components.map.ui.models.ShapeUi
import kotlinx.collections.immutable.ImmutableList

data class MappingUi(
    val planUrl: String,
    val color: String,
    val selectedColor: String,
    val labelSelected: String?,
    val started: Boolean,
    val start: OffsetUi,
    val end: OffsetUi,
    val pictoSize: Int,
    val shapes: ImmutableList<ShapeUi>,
    val pictograms: ImmutableList<PictogramUi>
)
