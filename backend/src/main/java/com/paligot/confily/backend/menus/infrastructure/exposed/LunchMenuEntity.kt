package com.paligot.confily.backend.menus.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import java.util.UUID

class LunchMenuEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<LunchMenuEntity>(LunchMenusTable) {
        fun findByEvent(eventId: UUID): SizedIterable<LunchMenuEntity> = this
            .find { LunchMenusTable.eventId eq eventId }
    }

    var eventId by LunchMenusTable.eventId
    var event by EventEntity.Companion referencedOn LunchMenusTable.eventId
    var date by LunchMenusTable.date
    var name by LunchMenusTable.name
    var dish by LunchMenusTable.dish
    var accompaniment by LunchMenusTable.accompaniment
    var dessert by LunchMenusTable.dessert
    var displayOrder by LunchMenusTable.displayOrder
    var createdAt by LunchMenusTable.createdAt
}
