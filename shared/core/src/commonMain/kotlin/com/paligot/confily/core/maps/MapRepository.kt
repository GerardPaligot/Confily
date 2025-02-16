package com.paligot.confily.core.maps

import com.paligot.confily.core.kvalue.ConferenceSettings
import com.paligot.confily.core.maps.entities.MapFilledItem
import com.paligot.confily.core.maps.entities.MapItemList
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface MapRepository {
    fun maps(): Flow<MapItemList>
    fun mapsFilled(): Flow<ImmutableList<MapFilledItem>>

    object Factory {
        fun create(settings: ConferenceSettings, mapDao: MapDao): MapRepository =
            MapRepositoryImpl(settings, mapDao)
    }
}
