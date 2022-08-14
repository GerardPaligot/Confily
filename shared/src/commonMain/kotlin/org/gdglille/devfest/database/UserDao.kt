package org.gdglille.devfest.database

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.models.UserNetworkingUi
import org.gdglille.devfest.models.UserProfileUi
import org.gdglille.devfest.toByteArray
import org.gdglille.devfest.toNativeImage

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

    fun fetchUserPreview(): UserProfileUi? =
        db.ticketQueries.selectTicket(eventId, mapper = { _, _, _, _, firstname, lastname, _, _ ->
            return@selectTicket UserProfileUi(
                email = "",
                firstName = firstname ?: "",
                lastName = lastname ?: "",
                company = "",
                qrCode = null
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

    fun deleteNetworking(email: String) = db.userQueries.deleteNetwork(email)
}
