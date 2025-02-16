package com.paligot.confily.core.maps

import com.paligot.confily.core.kvalue.ConferenceSettings
import com.paligot.confily.core.maps.entities.MapFilledItem
import com.paligot.confily.core.maps.entities.MapItemList
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map

class MapRepositoryImpl(
    private val settings: ConferenceSettings,
    private val mapDao: MapDao
) : MapRepository {
    override fun maps(): Flow<MapItemList> = settings.fetchEventId()
        .flatMapConcat { mapDao.fetchMapItems(eventId = it) }

    override fun mapsFilled(): Flow<ImmutableList<MapFilledItem>> = settings.fetchEventId()
        .flatMapConcat { mapDao.fetchMapFilled(eventId = it) }
        .map { it.toImmutableList() }
}
