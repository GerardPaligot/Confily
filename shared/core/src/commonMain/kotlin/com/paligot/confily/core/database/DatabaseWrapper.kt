package com.paligot.confily.core.database

import com.paligot.confily.db.Conferences4HallDatabase

expect class DatabaseWrapper {
    fun createDb(): Conferences4HallDatabase
}
