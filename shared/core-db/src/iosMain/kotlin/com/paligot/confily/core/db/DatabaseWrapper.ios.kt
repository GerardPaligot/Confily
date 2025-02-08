package com.paligot.confily.core.db

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import co.touchlab.sqliter.DatabaseConfiguration
import com.paligot.confily.db.ConfilyDatabase
import com.paligot.confily.db.Event
import com.paligot.confily.db.EventSession
import com.paligot.confily.db.Partner

actual class DatabaseWrapper {
    actual fun createDb(inMemory: Boolean): ConfilyDatabase {
        val driver = NativeSqliteDriver(
            schema = ConfilyDatabase.Schema,
            name = "confily.db",
            onConfiguration = { config: DatabaseConfiguration ->
                config.copy(
                    extendedConfig = DatabaseConfiguration.Extended(foreignKeyConstraints = true)
                )
            }
        )
        return ConfilyDatabase.invoke(
            driver = driver,
            EventAdapter = Event.Adapter(formatted_addressAdapter = listOfStringsAdapter),
            EventSessionAdapter = EventSession.Adapter(formatted_addressAdapter = listOfStringsAdapter),
            PartnerAdapter = Partner.Adapter(formatted_addressAdapter = listOfStringsAdapter)
        )
    }
}
