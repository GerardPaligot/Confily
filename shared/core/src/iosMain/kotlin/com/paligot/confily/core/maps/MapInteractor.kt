package com.paligot.confily.core.maps

import com.paligot.confily.core.maps.entities.MapFilledItem
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

class MapInteractor(
    private val repository: MapRepository
) {
    @NativeCoroutines
    fun mapsFilled(): Flow<ImmutableList<MapFilledItem>> = repository.mapsFilled()
}
