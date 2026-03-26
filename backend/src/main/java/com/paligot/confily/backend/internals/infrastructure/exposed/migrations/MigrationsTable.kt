package com.paligot.confily.backend.internals.infrastructure.exposed.migrations

import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object MigrationsTable : UUIDTable("schema_migrations") {
    val migrationId = varchar("migration_id", 255).uniqueIndex()
    val description = text("description")
    val appliedAt = timestamp("applied_at").clientDefault { Clock.System.now() }
}
