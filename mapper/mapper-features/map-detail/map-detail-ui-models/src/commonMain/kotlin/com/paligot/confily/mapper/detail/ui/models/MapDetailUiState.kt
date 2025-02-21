package com.paligot.confily.mapper.detail.ui.models

import com.paligot.confily.style.components.map.ui.models.MappingType

data class MapDetailUiState(
    val name: String = "",
    val type: MappingType? = null,
    val mapping: MappingUi? = null,
    val filledMapUrl: String? = null,
    val controller: ControllerUi = ControllerUi(),
    val enabled: Boolean = true
)
