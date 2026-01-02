package com.paligot.confily.backend.map.application

import com.paligot.confily.backend.map.domain.MapRepository
import com.paligot.confily.backend.map.infrastructure.firestore.MapEntity
import com.paligot.confily.backend.map.infrastructure.firestore.MapFirestore
import com.paligot.confily.backend.map.infrastructure.firestore.convertToModel
import com.paligot.confily.models.EventMap

class MapRepositoryDefault(
    private val mapFirestore: MapFirestore
) : MapRepository {
    override fun list(eventId: String): List<EventMap> = mapFirestore.getAll(eventId).map(MapEntity::convertToModel)
}
