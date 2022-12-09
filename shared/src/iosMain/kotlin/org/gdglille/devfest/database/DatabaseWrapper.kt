package org.gdglille.devfest.database

import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.db.Event
import org.gdglille.devfest.db.Partner
import org.gdglille.devfest.db.Schedule

actual class DatabaseWrapper {
    actual fun createDb(): Conferences4HallDatabase {
        val driver = NativeSqliteDriver(Conferences4HallDatabase.Schema, "conferences4hall.db")
        return Conferences4HallDatabase.invoke(
            driver,
            Event.Adapter(formatted_addressAdapter = listOfStringsAdapter),
            Partner.Adapter(formatted_addressAdapter = listOfStringsAdapter),
            Schedule.Adapter(speakersAdapter = listOfStringsAdapter, speakers_avatarAdapter = listOfStringsAdapter)
        )
    }
}
