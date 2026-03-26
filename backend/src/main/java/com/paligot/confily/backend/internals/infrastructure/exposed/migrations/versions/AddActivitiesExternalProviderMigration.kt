package com.paligot.confily.backend.internals.infrastructure.exposed.migrations.versions

import com.paligot.confily.backend.activities.infrastructure.exposed.ActivitiesTable
import com.paligot.confily.backend.internals.infrastructure.exposed.migrations.Migration
import org.jetbrains.exposed.sql.SchemaUtils

object AddActivitiesExternalProviderMigration : Migration {
    override val id = "20260326_add_activities_external_provider"
    override val description =
        "Add external_id and external_provider columns to activities table for partners-connect integration"

    override fun up() {
        SchemaUtils.createMissingTablesAndColumns(ActivitiesTable)
    }
}
