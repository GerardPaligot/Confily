---
name: exposed-migration
description: "Use this skill when adding, removing, or modifying columns, indexes, or tables in any Exposed table object (files under infrastructure/exposed/ ending in Table.kt). Explains when a migration is required, how to create one following the project convention, and how to register it so it runs automatically at startup."
---

# Exposed Migration Skill

## When a Migration Is Required

A migration **must** be created whenever you change an Exposed table definition in a way that alters the existing database schema. `SchemaUtils.create()` only creates tables that do not exist yet — it never modifies existing ones.

| Change | Migration needed? |
|---|---|
| Add a new `Table` object | No — `SchemaUtils.create()` handles it |
| Add a column to an existing table | **Yes** |
| Remove a column from an existing table | **Yes** (manual `ALTER TABLE DROP COLUMN`) |
| Change a column type or constraint | **Yes** |
| Add or remove an index on an existing table | **Yes** |
| Rename a column | **Yes** |
| Add a new nullable column with a default | **Yes** |

If you skip the migration, the new column/index will exist in code but not in the production database, causing runtime crashes on the next deployment.

## Project Infrastructure

The migration system lives at:

```
backend/src/main/java/com/paligot/confily/backend/internals/infrastructure/exposed/migrations/
  Migration.kt           — interface every migration implements
  MigrationsTable.kt     — schema_migrations table that tracks what has been applied
  MigrationManager.kt    — runs pending migrations at startup, records applied ones
  MigrationRegistry.kt   — ordered list of all migrations
  versions/              — one file per migration
```

`DatabaseFactory.init()` calls `MigrationManager(MigrationRegistry.allMigrations).migrate(database)` after `SchemaUtils.create()`. Each migration is run exactly once: its `id` is written to `schema_migrations` on success and skipped on all subsequent startups.

## How to Create a Migration

### 1. Create the migration class

Add a new file in `versions/` named after what the migration does:

```kotlin
// backend/.../migrations/versions/AddMyNewColumnMigration.kt
package com.paligot.confily.backend.internals.infrastructure.exposed.migrations.versions

import com.paligot.confily.backend.some.infrastructure.exposed.SomeTable
import com.paligot.confily.backend.internals.infrastructure.exposed.migrations.Migration
import org.jetbrains.exposed.sql.SchemaUtils

object AddMyNewColumnMigration : Migration {
    override val id = "YYYYMMDD_add_my_new_column"
    override val description = "Add <column_name> to <table_name> for <reason>"

    override fun up() {
        SchemaUtils.createMissingTablesAndColumns(SomeTable)
    }
}
```

### 2. Register it in MigrationRegistry

Open `MigrationRegistry.kt` and append the new migration **at the end** of `allMigrations`:

```kotlin
val allMigrations: List<Migration> = listOf(
    // ... existing migrations ...
    AddMyNewColumnMigration
)
```

Do **not** reorder existing entries — migrations are applied in list order and each runs only once.

## ID Convention

Use `YYYYMMDD_short_snake_case_description`, e.g.:

- `20260326_add_activities_external_provider`
- `20260401_add_partner_video_url`

The `id` must be **unique** across all migrations and **sortable chronologically**. The `MigrationManager` sorts by `id` before applying, so the date prefix guarantees correct ordering.

## Patterns for Common Changes

### Add nullable columns to an existing table

Just add the column to the table object and call `SchemaUtils.createMissingTablesAndColumns`:

```kotlin
override fun up() {
    SchemaUtils.createMissingTablesAndColumns(TargetTable)
}
```

### Execute raw SQL (e.g. drop a column, add a partial index)

Use `exec()` inside a `Transaction`:

```kotlin
override fun up() {
    exec("ALTER TABLE my_table DROP COLUMN IF EXISTS old_column")
}
```

`up()` is always called inside an active transaction (from `MigrationManager`), so `exec()` is available directly.

### Data migration

Perform data manipulation with Exposed DSL inside `up()`:

```kotlin
override fun up() {
    SchemaUtils.createMissingTablesAndColumns(TargetTable)
    TargetTable.update {
        it[newColumn] = "default_value"
    }
}
```

## Rules

1. **Never modify an already-applied migration.** Once an `id` is in `schema_migrations`, that migration will never run again. Fix mistakes with a new migration.
2. **Never reorder `MigrationRegistry.allMigrations`.** The list order controls application order for new databases.
3. **Keep `up()` idempotent where possible.** Use `IF NOT EXISTS`, `IF EXISTS`, and `createMissingTablesAndColumns` so re-runs on a partially migrated database are safe.
4. **One logical change per migration.** Small, focused migrations are easier to debug and roll forward.
5. **Always provide a meaningful `description`.** It appears in logs and `schema_migrations` for auditability.
