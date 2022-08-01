package org.gdglille.devfest.database

import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.db.Schedule

actual class DatabaseWrapper {
    actual fun createDb(): Conferences4HallDatabase {
        val driver = NativeSqliteDriver(Conferences4HallDatabase.Schema, "conferences4hall.db")
        return Conferences4HallDatabase.invoke(
            driver,
            Schedule.Adapter(speakersAdapter = listOfStringsAdapter, speakers_avatarAdapter = listOfStringsAdapter)
        )
    }
}
