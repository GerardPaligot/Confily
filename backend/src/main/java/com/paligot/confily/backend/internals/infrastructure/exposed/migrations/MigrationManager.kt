package com.paligot.confily.backend.internals.infrastructure.exposed.migrations

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

class MigrationManager(private val migrations: List<Migration>) {
    private val logger = LoggerFactory.getLogger(MigrationManager::class.java)

    fun migrate(database: Database) {
        transaction(database) {
            SchemaUtils.createMissingTablesAndColumns(MigrationsTable)

            val applied = MigrationsTable
                .selectAll()
                .map { it[MigrationsTable.migrationId] }
                .toSet()

            migrations
                .sortedBy { it.id }
                .filter { it.id !in applied }
                .forEach { migration ->
                    logger.info("Applying migration: ${migration.id} - ${migration.description}")
                    @Suppress("TooGenericExceptionCaught")
                    try {
                        migration.up()
                        MigrationsTable.insert {
                            it[migrationId] = migration.id
                            it[description] = migration.description
                        }
                        logger.info("Successfully applied migration: ${migration.id}")
                    } catch (e: Exception) {
                        logger.error("Failed to apply migration: ${migration.id}", e)
                        throw IllegalStateException("Migration ${migration.id} failed: ${e.message}", e)
                    }
                }
        }
    }
}
