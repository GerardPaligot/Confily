package com.paligot.confily.backend.internals.infrastructure.exposed.migrations.versions

import com.paligot.confily.backend.events.infrastructure.exposed.EventsTable
import com.paligot.confily.backend.internals.infrastructure.exposed.migrations.Migration
import org.jetbrains.exposed.sql.SchemaUtils

object AddEventTimezoneMigration : Migration {
    override val id = "20260403_add_event_timezone"
    override val description = "Add timezone column to events table for OpenPlanner timezone support"

    override fun up() {
        SchemaUtils.createMissingTablesAndColumns(EventsTable)
    }
}
