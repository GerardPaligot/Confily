package com.paligot.conferences.database

import com.paligot.conferences.db.Conferences4HallDatabase
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock

class UserDao(private val db: Conferences4HallDatabase, private val settings: Settings, private val eventId: String) {
    fun fetchQrCode(email: String): ByteArray? = db.userQueries.selectQrCode(email).executeAsOneOrNull()?.qrcode
    fun insertUser(email: String, image: ByteArray) {
        db.userQueries.insertUser(email, image)
        settings["EMAIL"] = email
    }

    fun fetchLastEmail(): String? = settings.getStringOrNull("EMAIL")

    fun fetchNetworking(): Flow<List<String>> =
        db.userQueries.selectAll(eventId) { email, _, _ -> email }.asFlow().mapToList()

    fun insertEmailNetworking(email: String) = db.userQueries.insertNetwork(email, eventId, Clock.System.now().epochSeconds)
}
