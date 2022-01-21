package com.paligot.conferences.database

import android.content.Context
import com.paligot.conferences.db.Conferences4HallDatabase
import com.paligot.conferences.db.Schedule
import com.squareup.sqldelight.android.AndroidSqliteDriver

lateinit var appContext: Context

actual fun createDb(): Conferences4HallDatabase {
    val driver = AndroidSqliteDriver(Conferences4HallDatabase.Schema, appContext, "conferences4hall.db")
    return Conferences4HallDatabase.invoke(driver, Schedule.Adapter(speakersAdapter = listOfStringsAdapter))
}
