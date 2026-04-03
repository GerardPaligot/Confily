package com.paligot.confily.backend.internals.infrastructure.exposed.migrations

import com.paligot.confily.backend.internals.infrastructure.exposed.migrations.versions.AddActivitiesExternalProviderMigration
import com.paligot.confily.backend.internals.infrastructure.exposed.migrations.versions.AddEventTimezoneMigration

object MigrationRegistry {
    val allMigrations: List<Migration> = listOf(
        AddActivitiesExternalProviderMigration,
        AddEventTimezoneMigration
    )
}
