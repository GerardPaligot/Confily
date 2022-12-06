package org.gdglille.devfest.database

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.db.Event
import org.gdglille.devfest.db.Partner
import org.gdglille.devfest.db.Schedule

actual class DatabaseWrapper(val context: Context) {
    actual fun createDb(): Conferences4HallDatabase {
        val driver = AndroidSqliteDriver(Conferences4HallDatabase.Schema, context, "conferences4hall.db")
        return Conferences4HallDatabase.invoke(
            driver,
            Event.Adapter(formatted_addressAdapter = listOfStringsAdapter),
            Partner.Adapter(formatted_addressAdapter = listOfStringsAdapter),
            Schedule.Adapter(speakersAdapter = listOfStringsAdapter, speakers_avatarAdapter = listOfStringsAdapter)
        )
    }
}
