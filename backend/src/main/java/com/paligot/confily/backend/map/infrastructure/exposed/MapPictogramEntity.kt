package com.paligot.confily.backend.map.infrastructure.exposed

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class MapPictogramEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<MapPictogramEntity>(MapPictogramsTable) {
        fun findByMapId(mapId: UUID) = this.find { MapPictogramsTable.mapId eq mapId }
    }

    var mapId by MapPictogramsTable.mapId
    var map by MapEntity.Companion referencedOn MapPictogramsTable.mapId
    var displayOrder by MapPictogramsTable.displayOrder
    var name by MapPictogramsTable.name
    var description by MapPictogramsTable.description
    var positionX by MapPictogramsTable.positionX
    var positionY by MapPictogramsTable.positionY
    var type by MapPictogramsTable.type
    var createdAt by MapPictogramsTable.createdAt
}
