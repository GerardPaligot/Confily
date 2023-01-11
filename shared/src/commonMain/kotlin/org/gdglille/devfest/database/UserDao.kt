package org.gdglille.devfest.database

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.models.UserNetworkingUi
import org.gdglille.devfest.models.UserProfileUi
import org.gdglille.devfest.toByteArray
import org.gdglille.devfest.toNativeImage

class UserDao(private val db: Conferences4HallDatabase, private val eventId: String) {
    fun fetchProfile(eventId: String): Flow<UserProfileUi?> =
        db.userQueries.selectProfile(eventId, mapper = { _, email, firstname, lastname, company, qrcode ->
            return@selectProfile UserProfileUi(
                email = email,
                firstName = firstname,
                lastName = lastname,
                company = company ?: "",
                qrCode = qrcode.toNativeImage()
            )
        }).asFlow().mapToOneOrNull()

    fun fetchUserPreview(eventId: String): Flow<UserProfileUi?> =
        db.ticketQueries.selectTicket(eventId, mapper = { _, _, _, _, firstname, lastname, _, _ ->
            return@selectTicket UserProfileUi(
                email = "",
                firstName = firstname ?: "",
                lastName = lastname ?: "",
                company = "",
                qrCode = null
            )
        }).asFlow().mapToOneOrNull()

    fun insertUser(eventId: String, user: UserProfileUi) {
        db.userQueries.insertProfile(
            eventId,
            user.email,
            user.firstName,
            user.lastName,
            user.company,
            user.qrCode!!.toByteArray()
        )
    }

    fun fetchNetworking(eventId: String): Flow<List<UserNetworkingUi>> =
        db.userQueries.selectAll(eventId) { email, firstName, lastName, company, _, _ ->
            UserNetworkingUi(
                email,
                firstName,
                lastName,
                company ?: ""
            )
        }.asFlow().mapToList()

    fun insertEmailNetworking(eventId: String, userNetworkingUi: UserNetworkingUi) =
        db.userQueries.insertNetwork(
            userNetworkingUi.email,
            userNetworkingUi.firstName,
            userNetworkingUi.lastName,
            userNetworkingUi.company,
            eventId,
            Clock.System.now().epochSeconds
        )

    fun deleteNetworking(eventId: String, email: String) =
        db.userQueries.deleteNetwork(eventId, email)

    @Deprecated(message = "")
    fun fetchProfile(): Flow<UserProfileUi?> = fetchProfile(eventId)

    @Deprecated(message = "")
    fun fetchUserPreview(): Flow<UserProfileUi?> = fetchUserPreview(eventId)

    @Deprecated(message = "")
    fun insertUser(user: UserProfileUi) = insertUser(eventId, user)

    @Deprecated(message = "")
    fun fetchNetworking(): Flow<List<UserNetworkingUi>> = fetchNetworking(eventId)

    @Deprecated(message = "")
    fun insertEmailNetworking(userNetworkingUi: UserNetworkingUi) =
        insertEmailNetworking(eventId, userNetworkingUi)

    @Deprecated(message = "")
    fun deleteNetworking(email: String) = deleteNetworking(eventId, email)
}
