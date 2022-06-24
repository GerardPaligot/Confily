package org.gdglille.devfest.database

import org.gdglille.devfest.db.Conferences4HallDatabase

expect class DatabaseWrapper {
    fun createDb(): Conferences4HallDatabase
}
