package org.gdglille.devfest.database

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import co.touchlab.sqliter.DatabaseConfiguration
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.db.Event
import org.gdglille.devfest.db.Partner

actual class DatabaseWrapper {
    actual fun createDb(): Conferences4HallDatabase {
        val driver = NativeSqliteDriver(
            schema = Conferences4HallDatabase.Schema,
            name = "conferences4hall.db",
            onConfiguration = { config: DatabaseConfiguration ->
                config.copy(
                    extendedConfig = DatabaseConfiguration.Extended(foreignKeyConstraints = true)
                )
            }
        )
        return Conferences4HallDatabase.invoke(
            driver,
            Event.Adapter(formatted_addressAdapter = listOfStringsAdapter),
            Partner.Adapter(formatted_addressAdapter = listOfStringsAdapter)
        )
    }
}
