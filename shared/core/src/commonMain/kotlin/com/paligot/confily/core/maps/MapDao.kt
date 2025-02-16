package com.paligot.confily.core.maps

import com.paligot.confily.core.maps.entities.MapFilledItem
import com.paligot.confily.core.maps.entities.MapItemList
import com.paligot.confily.models.EventMap
import kotlinx.coroutines.flow.Flow

interface MapDao {
    fun fetchMapItems(eventId: String): Flow<MapItemList>
    fun fetchMapFilled(eventId: String): Flow<List<MapFilledItem>>
    fun insertMaps(eventId: String, maps: List<EventMap>)
}
