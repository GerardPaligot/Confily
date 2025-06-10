package com.paligot.confily.backend.map.application

import com.paligot.confily.backend.internals.infrastructure.firestore.MapEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.MapFirestore
import com.paligot.confily.backend.map.domain.MapRepository
import com.paligot.confily.models.EventMap

class MapRepositoryDefault(
    private val mapFirestore: MapFirestore
) : MapRepository {
    override fun list(eventId: String): List<EventMap> = mapFirestore.getAll(eventId).map(MapEntity::convertToModel)
}
