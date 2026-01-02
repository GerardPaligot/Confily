package com.paligot.confily.backend.map.infrastructure.exposed

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import java.util.UUID

class MapShapeEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<MapShapeEntity>(MapShapesTable) {
        fun findByMapId(mapId: UUID): SizedIterable<MapShapeEntity> = this
            .find { MapShapesTable.mapId eq mapId }
    }

    var mapId by MapShapesTable.mapId
    var map by MapEntity.Companion referencedOn MapShapesTable.mapId
    var displayOrder by MapShapesTable.displayOrder
    var name by MapShapesTable.name
    var description by MapShapesTable.description
    var startX by MapShapesTable.startX
    var startY by MapShapesTable.startY
    var endX by MapShapesTable.endX
    var endY by MapShapesTable.endY
    var type by MapShapesTable.type
    var createdAt by MapShapesTable.createdAt
}
