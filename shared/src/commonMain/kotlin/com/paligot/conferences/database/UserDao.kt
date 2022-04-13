package com.paligot.conferences.database

import com.paligot.conferences.db.Conferences4HallDatabase
import com.paligot.conferences.models.UserNetworkingUi
import com.paligot.conferences.models.UserProfileUi
import com.paligot.conferences.toByteArray
import com.paligot.conferences.toNativeImage
import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock

class UserDao(private val db: Conferences4HallDatabase, private val settings: Settings, private val eventId: String) {
    fun fetchUser(emailId: String): UserProfileUi? =
        db.userQueries.selectUser(emailId, mapper = { email, firstname, lastname, company, qrcode ->
            return@selectUser UserProfileUi(
                email = email,
                firstName = firstname,
                lastName = lastname,
                company = company ?: "",
                qrCode = qrcode.toNativeImage()
            )
        }).executeAsOneOrNull()

    fun insertUser(user: UserProfileUi) {
        db.userQueries.insertUser(user.email, user.firstName, user.lastName, user.company, user.qrCode!!.toByteArray())
        settings["EMAIL"] = user.email
    }

    fun fetchLastEmail(): String? = settings.getStringOrNull("EMAIL")

    fun fetchNetworking(): Flow<List<UserNetworkingUi>> =
        db.userQueries.selectAll(eventId) { email, firstName, lastName, company, _, _ ->
            UserNetworkingUi(
                email,
                firstName,
                lastName,
                company ?: ""
            )
        }.asFlow().mapToList()

    fun insertEmailNetworking(userNetworkingUi: UserNetworkingUi) =
        db.userQueries.insertNetwork(
            userNetworkingUi.email,
            userNetworkingUi.firstName,
            userNetworkingUi.lastName,
            userNetworkingUi.company,
            eventId,
            Clock.System.now().epochSeconds
        )
}
