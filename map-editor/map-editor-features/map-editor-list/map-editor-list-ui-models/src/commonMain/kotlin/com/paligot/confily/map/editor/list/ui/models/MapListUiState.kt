package com.paligot.confily.map.editor.list.ui.models

import kotlinx.collections.immutable.persistentListOf

data class MapListUiState(
    val loading: Boolean = false,
    val uiModel: MapListUi = MapListUi(creation = null, items = persistentListOf())
)
