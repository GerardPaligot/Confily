package com.paligot.confily.infos.ui.models

import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

data class LabellingState(
    val label: String? = null,
    val description: String? = null
)

data class MapItemUiState(
    val labelling: ImmutableMap<String, LabellingState> = persistentMapOf(),
    val uiModel: MapUi = MapUi(persistentListOf())
)
