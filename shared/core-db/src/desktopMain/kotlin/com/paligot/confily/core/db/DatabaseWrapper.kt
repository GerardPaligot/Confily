package com.paligot.confily.core.db

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.paligot.confily.db.ConfilyDatabase
import com.paligot.confily.db.Event
import com.paligot.confily.db.EventSession
import com.paligot.confily.db.Partner

actual class DatabaseWrapper {
    actual fun createDb(): ConfilyDatabase = ConfilyDatabase.invoke(
        driver = JdbcSqliteDriver(
            url = "jdbc:sqlite:confily.db",
            schema = ConfilyDatabase.Schema
        ),
        EventAdapter = Event.Adapter(formatted_addressAdapter = listOfStringsAdapter),
        EventSessionAdapter = EventSession.Adapter(formatted_addressAdapter = listOfStringsAdapter),
        PartnerAdapter = Partner.Adapter(formatted_addressAdapter = listOfStringsAdapter)
    )
}
