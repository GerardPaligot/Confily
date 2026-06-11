package com.paligot.confily.core.test

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.paligot.confily.core.db.listOfStringsAdapter
import com.paligot.confily.db.ConfilyDatabase
import com.paligot.confily.db.Event
import com.paligot.confily.db.EventSession
import com.paligot.confily.db.Partner

/**
 * Builds a fresh in-memory ConfilyDatabase with foreign-key enforcement ON,
 * matching the production drivers (Android `setForeignKeyConstraintsEnabled(true)`,
 * iOS `foreignKeyConstraints = true`). FK enforcement is what makes the sync
 * cleanup-ordering bug reproducible in tests. Adapters mirror `DatabaseWrapper`.
 */
fun inMemoryDatabase(): ConfilyDatabase {
    val driver = JdbcSqliteDriver(url = JdbcSqliteDriver.IN_MEMORY, schema = ConfilyDatabase.Schema)
    driver.execute(null, "PRAGMA foreign_keys=ON;", 0)
    return ConfilyDatabase(
        driver = driver,
        EventAdapter = Event.Adapter(formatted_addressAdapter = listOfStringsAdapter),
        EventSessionAdapter = EventSession.Adapter(formatted_addressAdapter = listOfStringsAdapter),
        PartnerAdapter = Partner.Adapter(formatted_addressAdapter = listOfStringsAdapter)
    )
}
