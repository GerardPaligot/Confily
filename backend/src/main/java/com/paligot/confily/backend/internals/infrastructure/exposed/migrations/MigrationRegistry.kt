package com.paligot.confily.backend.internals.infrastructure.exposed.migrations

import com.paligot.confily.backend.internals.infrastructure.exposed.migrations.versions.AddActivitiesExternalProviderMigration

object MigrationRegistry {
    val allMigrations: List<Migration> = listOf(
        AddActivitiesExternalProviderMigration
    )
}
