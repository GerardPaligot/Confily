package com.paligot.confily.mapper.list.ui.models

import kotlinx.collections.immutable.ImmutableList

data class MapListUi(
    val creation: MapCreationUi?,
    val items: ImmutableList<MapItemUi>
)
