package com.paligot.confily.backend.map.application

import com.paligot.confily.backend.map.domain.MapRepository
import com.paligot.confily.backend.map.infrastructure.exposed.MapEntity
import com.paligot.confily.backend.map.infrastructure.exposed.MapPictogramEntity
import com.paligot.confily.backend.map.infrastructure.exposed.MapPictogramsTable
import com.paligot.confily.backend.map.infrastructure.exposed.MapShapeEntity
import com.paligot.confily.backend.map.infrastructure.exposed.MapShapesTable
import com.paligot.confily.backend.map.infrastructure.exposed.MapsTable
import com.paligot.confily.backend.map.infrastructure.exposed.toModel
import com.paligot.confily.models.EventMap
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class MapRepositoryExposed(private val database: Database) : MapRepository {

    override fun list(eventId: String): List<EventMap> = transaction(db = database) {
        val eventUuid = UUID.fromString(eventId)
        MapEntity
            .findByEvent(eventUuid)
            .orderBy(MapsTable.displayOrder to SortOrder.ASC)
            .map { mapEntity ->
                val shapes = MapShapeEntity
                    .findByMapId(mapEntity.id.value)
                    .orderBy(MapShapesTable.displayOrder to SortOrder.ASC)
                    .map { it.toModel() }
                val pictograms = MapPictogramEntity
                    .findByMapId(mapEntity.id.value)
                    .orderBy(MapPictogramsTable.displayOrder to SortOrder.ASC)
                    .map { it.toModel() }
                mapEntity.toModel(shapes, pictograms)
            }
    }
}
