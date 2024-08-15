package org.gdglille.devfest.database

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.db.Event
import org.gdglille.devfest.db.EventSession
import org.gdglille.devfest.db.Partner

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
