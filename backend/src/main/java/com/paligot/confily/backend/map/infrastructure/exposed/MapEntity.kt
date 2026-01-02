package com.paligot.confily.backend.map.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and
import java.util.UUID

class MapEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<MapEntity>(MapsTable) {
        fun findByEvent(eventId: UUID): SizedIterable<MapEntity> = this
            .find { MapsTable.eventId eq eventId }

        fun findById(eventId: UUID, mapId: UUID): MapEntity? = this
            .find { (MapsTable.eventId eq eventId) and (MapsTable.id eq mapId) }
            .firstOrNull()
    }

    var eventId by MapsTable.eventId
    var event by EventEntity.Companion referencedOn MapsTable.eventId
    var name by MapsTable.name
    var description by MapsTable.description
    var color by MapsTable.color
    var colorSelected by MapsTable.colorSelected
    var filename by MapsTable.filename
    var url by MapsTable.url
    var filledUrl by MapsTable.filledUrl
    var displayOrder by MapsTable.displayOrder
    var pictoSize by MapsTable.pictoSize
    var createdAt by MapsTable.createdAt
    var updatedAt by MapsTable.updatedAt
}
