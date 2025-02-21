package com.paligot.confily.mapper.detail.ui.models

import com.paligot.confily.style.components.map.ui.models.MappingType
import com.paligot.confily.style.components.map.ui.models.PictogramType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class ControllerUi(
    val name: String = "",
    val color: String = "",
    val colorSelected: String = "",
    val shapeOrder: String? = null,
    val shapeName: String? = null,
    val shapeDescription: String? = null,
    val modeSelected: MappingMode? = null,
    val modes: ImmutableList<MappingMode> = MappingMode.entries.toImmutableList(),
    val typeSelected: MappingType? = null,
    val types: ImmutableList<MappingType> = MappingType.entries.toImmutableList(),
    val pictogramSelected: PictogramType? = null,
    val pictogramSize: String = ""
)
