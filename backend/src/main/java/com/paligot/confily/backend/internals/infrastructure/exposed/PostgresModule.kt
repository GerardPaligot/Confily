package com.paligot.confily.backend.internals.infrastructure.exposed

import org.jetbrains.exposed.sql.Database

object PostgresModule {
    private lateinit var _database: Database

    fun init(database: Database) {
        _database = database
    }

    val database by lazy { _database }
}
