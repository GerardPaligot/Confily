package com.paligot.confily.infos.ui.models

import com.paligot.confily.style.components.map.ui.models.PictogramUi
import com.paligot.confily.style.components.map.ui.models.ShapeUi
import kotlinx.collections.immutable.ImmutableList

data class MapItemUi(
    val id: String,
    val name: String,
    val url: String,
    val colorHex: String,
    val selectedColorHex: String,
    val pictoSize: Int,
    val shapes: ImmutableList<ShapeUi>,
    val pictograms: ImmutableList<PictogramUi>,
    val labels: ImmutableList<String>
)
