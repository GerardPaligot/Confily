package org.gdglille.devfest.database

import android.content.Context
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.db.Schedule
import com.squareup.sqldelight.android.AndroidSqliteDriver

actual class DatabaseWrapper(val context: Context) {
    actual fun createDb(): Conferences4HallDatabase {
        val driver = AndroidSqliteDriver(Conferences4HallDatabase.Schema, context, "conferences4hall.db")
        return Conferences4HallDatabase.invoke(driver, Schedule.Adapter(speakersAdapter = listOfStringsAdapter))
    }
}
