package com.paligot.confily.core.db

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.paligot.confily.db.ConfilyDatabase
import com.paligot.confily.db.Event
import com.paligot.confily.db.EventSession
import com.paligot.confily.db.Partner

actual class DatabaseWrapper(
    private val context: Context,
    private val name: String?
) {
    actual fun createDb(inMemory: Boolean): ConfilyDatabase {
        val driver = AndroidSqliteDriver(
            schema = ConfilyDatabase.Schema,
            context = context,
            name = name,
            callback = object : AndroidSqliteDriver.Callback(ConfilyDatabase.Schema) {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    db.setForeignKeyConstraintsEnabled(true)
                }
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
