package com.paligot.confily.backend.menus.application

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.menus.domain.MenuAdminRepository
import com.paligot.confily.backend.menus.infrastructure.exposed.LunchMenuEntity
import com.paligot.confily.backend.menus.infrastructure.exposed.LunchMenusTable
import com.paligot.confily.models.inputs.LunchMenuInput
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class MenuAdminRepositoryExposed(private val database: Database) : MenuAdminRepository {
    override suspend fun update(eventId: String, menus: List<LunchMenuInput>): String = transaction(db = database) {
        val eventUuid = UUID.fromString(eventId)
        val event = EventEntity[eventUuid]

        // Delete all existing menus for this event
        LunchMenusTable.deleteWhere { LunchMenusTable.eventId eq eventUuid }
        // Create new menus
        menus.forEachIndexed { index, menuInput ->
            LunchMenuEntity.new {
                this.event = event
                this.name = menuInput.name
                this.dish = menuInput.dish
                this.accompaniment = menuInput.accompaniment
                this.dessert = menuInput.dessert
                this.date = LocalDate.parse(menuInput.date)
                this.displayOrder = index
            }
        }

        eventId
    }
}
