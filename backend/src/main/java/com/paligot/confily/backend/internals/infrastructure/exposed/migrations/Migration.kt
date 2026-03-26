package com.paligot.confily.backend.internals.infrastructure.exposed.migrations

/**
 * Represents a database migration that can be applied to evolve the database schema.
 * The [id] must be unique across all migrations and sortable in application order,
 * following the format "YYYYMMDD_description".
 */
interface Migration {
    val id: String
    val description: String

    /** Apply this migration. Called within an active transaction. */
    fun up()
}
