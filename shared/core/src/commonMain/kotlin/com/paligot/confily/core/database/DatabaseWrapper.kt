package com.paligot.confily.core.database

import com.paligot.confily.db.ConfilyDatabase

expect class DatabaseWrapper {
    fun createDb(): ConfilyDatabase
}
