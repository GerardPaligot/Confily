package com.paligot.confily.core.networking

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrDefault
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.paligot.confily.core.networking.entities.UserInfo
import com.paligot.confily.core.networking.entities.UserItem
import com.paligot.confily.core.networking.entities.UserTicket
import com.paligot.confily.db.ConfilyDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlin.coroutines.CoroutineContext

class UserDaoSQLDelight(
    private val db: ConfilyDatabase,
    private val dispatcher: CoroutineContext
) : UserDao {
    override fun fetchUser(eventId: String): Flow<UserInfo?> = db.userQueries
        .selectProfile(eventId, profileMapper)
        .asFlow()
        .mapToOneOrNull(dispatcher)

    override fun fetchUserTicket(eventId: String): Flow<UserTicket?> = db.ticketQueries
        .selectTicket(eventId, tickerMapper)
        .asFlow()
        .mapToOneOrNull(dispatcher)

    override fun fetchUsersScanned(eventId: String): Flow<List<UserItem>> = db.userQueries
        .selectAll(eventId, userItemMapper)
        .asFlow()
        .mapToList(dispatcher)

    override fun fetchCountUserScanned(eventId: String): Flow<Int> = db.userQueries
        .countNetworking(eventId)
        .asFlow()
        .mapToOneOrDefault(defaultValue = 0L, context = dispatcher)
        .map { it.toInt() }

    override fun getUsersScanned(eventId: String): List<UserItem> = db.userQueries
        .selectAll(eventId, userItemMapper)
        .executeAsList()

    override fun insertUser(eventId: String, user: UserInfo) {
        db.userQueries.insertProfile(
            event_id = eventId,
            email = user.email,
            firstname = user.firstName,
            lastname = user.lastName,
            company = user.company,
            qrcode = user.qrCode ?: byteArrayOf()
        )
    }

    override fun insertUserScanned(eventId: String, user: UserItem) =
        db.userQueries.insertNetwork(
            user.email,
            user.firstName,
            user.lastName,
            user.company,
            eventId,
            Clock.System.now().epochSeconds
        )

    override fun deleteUserByEmail(eventId: String, email: String) = db.userQueries
        .deleteNetwork(eventId, email)

    override fun getEmailProfile(eventId: String): String? = db.userQueries
        .selectProfile(eventId)
        .executeAsOneOrNull()
        ?.email
}
