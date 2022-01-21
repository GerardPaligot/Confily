package com.paligot.conferences.database

import com.paligot.conferences.db.Conferences4HallDatabase
import com.paligot.conferences.db.Schedule
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual fun createDb(): Conferences4HallDatabase {
    val driver = NativeSqliteDriver(Conferences4HallDatabase.Schema, "conferences4hall.db")
    return Conferences4HallDatabase.invoke(driver, Schedule.Adapter(speakersAdapter = listOfStringsAdapter))
}
