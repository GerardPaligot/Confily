package com.paligot.confily.core.networking

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.paligot.confily.core.Platform
import com.paligot.confily.db.ConfilyDatabase
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
    private val db: ConfilyDatabase,
    private val platform: Platform,
    private val dispatcher: CoroutineContext
) {
    fun fetchProfile(eventId: String): Flow<UserProfileUi?> =
        db.userQueries.selectProfile(eventId, profileMapper).asFlow().mapToOneOrNull(dispatcher)

    fun fetchUserPreview(eventId: String): Flow<UserProfileUi?> =
        db.ticketQueries.selectTicket(eventId, tickerMapper).asFlow().mapToOneOrNull(dispatcher)

    fun fetchNetworking(eventId: String): Flow<ImmutableList<UserNetworkingUi>> =
        db.userQueries.selectAll(eventId, userItemMapper).asFlow()
            .mapToList(dispatcher)
            .map { it.toImmutableList() }

    fun insertUser(
        eventId: String,
        email: String,
        firstName: String,
        lastName: String,
        company: String?,
        qrCode: ByteArray
    ) {
        db.userQueries.insertProfile(eventId, email, firstName, lastName, company, qrCode)
    }

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

    fun getEmailProfile(eventId: String): String? = db.userQueries
        .selectProfile(eventId)
        .executeAsOneOrNull()
        ?.email
}
