package com.paligot.conferences.database

import com.paligot.conferences.db.Conferences4HallDatabase

expect class DatabaseWrapper {
    fun createDb(): Conferences4HallDatabase
}