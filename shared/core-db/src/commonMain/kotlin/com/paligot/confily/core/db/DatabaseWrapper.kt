package com.paligot.confily.core.db

import com.paligot.confily.db.ConfilyDatabase

expect class DatabaseWrapper {
    fun createDb(inMemory: Boolean = false): ConfilyDatabase
}
