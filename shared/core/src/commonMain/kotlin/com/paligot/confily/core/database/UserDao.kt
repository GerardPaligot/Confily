package com.paligot.confily.core.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.paligot.confily.core.Platform
import com.paligot.confily.core.toByteArray
import com.paligot.confily.core.toNativeImage
import com.paligot.confily.db.Conferences4HallDatabase
import com.paligot.confily.models.ui.UserNetworkingUi
import com.paligot.confily.models.ui.UserProfileUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import okio.Path.Companion.toPath
import kotlin.coroutines.CoroutineContext

class UserDao(
    private val db: Conferences4HallDatabase,
    private val platform: Platform,
    private val dispatcher: CoroutineContext
) {
    fun getEmailProfile(eventId: String): String? = db.userQueries
        .selectProfile(eventId)
        .executeAsOneOrNull()
        ?.email

    fun fetchProfile(eventId: String): Flow<UserProfileUi?> =
        db.userQueries.selectProfile(
            eventId,
            mapper = { _, email, firstname, lastname, company, qrcode ->
                return@selectProfile UserProfileUi(
                    email = email,
                    firstName = firstname,
                    lastName = lastname,
                    company = company ?: "",
                    qrCode = qrcode.toNativeImage()
                )
            }
        ).asFlow().mapToOneOrNull(dispatcher)

    fun fetchUserPreview(eventId: String): Flow<UserProfileUi?> =
        db.ticketQueries.selectTicket(eventId, mapper = { _, _, _, _, firstname, lastname, _, _ ->
            return@selectTicket UserProfileUi(
                email = "",
                firstName = firstname ?: "",
                lastName = lastname ?: "",
                company = "",
                qrCode = null
            )
        }).asFlow().mapToOneOrNull(dispatcher)

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

    fun fetchNetworking(eventId: String): Flow<ImmutableList<UserNetworkingUi>> =
        db.userQueries.selectAll(eventId) { email, firstName, lastName, company, _, _ ->
            UserNetworkingUi(
                email,
                firstName,
                lastName,
                company ?: ""
            )
        }.asFlow().mapToList(dispatcher).map { it.toImmutableList() }

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

    fun exportNetworking(eventId: String): String {
        val users = db.userQueries.selectAll(eventId).executeAsList()
        val path = "${platform.fileEngine.tempFolderPath}/${Clock.System.now().epochSeconds}.csv"
        platform.fileEngine.fileSystem.write(path.toPath()) {
            users.forEach { user ->
                writeUtf8("${user.email};${user.firstname};${user.lastname};${user.company}")
            }
        }
        return path
    }
}
