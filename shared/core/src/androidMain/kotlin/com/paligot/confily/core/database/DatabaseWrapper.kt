package com.paligot.confily.core.database

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.paligot.confily.db.Conferences4HallDatabase
import com.paligot.confily.db.Event
import com.paligot.confily.db.EventSession
import com.paligot.confily.db.Partner

actual class DatabaseWrapper(val context: Context, val name: String?) {
    actual fun createDb(): Conferences4HallDatabase {
        val driver = AndroidSqliteDriver(
            schema = Conferences4HallDatabase.Schema,
            context = context,
            name = name,
            callback = object : AndroidSqliteDriver.Callback(Conferences4HallDatabase.Schema) {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    db.setForeignKeyConstraintsEnabled(true)
                }
            }
        )
        return Conferences4HallDatabase.invoke(
            driver = driver,
            EventAdapter = Event.Adapter(formatted_addressAdapter = listOfStringsAdapter),
            EventSessionAdapter = EventSession.Adapter(formatted_addressAdapter = listOfStringsAdapter),
            PartnerAdapter = Partner.Adapter(formatted_addressAdapter = listOfStringsAdapter)
        )
    }
}
