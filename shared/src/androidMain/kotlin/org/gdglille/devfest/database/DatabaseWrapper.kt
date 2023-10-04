package org.gdglille.devfest.database

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.db.Event
import org.gdglille.devfest.db.Partner

actual class DatabaseWrapper(val context: Context) {
    actual fun createDb(): Conferences4HallDatabase {
        val driver = AndroidSqliteDriver(
            schema = Conferences4HallDatabase.Schema,
            context = context,
            name = "conferences4hall.db",
            callback = object : AndroidSqliteDriver.Callback(Conferences4HallDatabase.Schema) {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    db.setForeignKeyConstraintsEnabled(true)
                }
            }
        )
        return Conferences4HallDatabase.invoke(
            driver,
            Event.Adapter(formatted_addressAdapter = listOfStringsAdapter),
            Partner.Adapter(formatted_addressAdapter = listOfStringsAdapter)
        )
    }
}
