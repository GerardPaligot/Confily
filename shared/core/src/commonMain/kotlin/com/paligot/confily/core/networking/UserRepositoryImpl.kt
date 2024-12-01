package com.paligot.confily.core.networking

import com.paligot.confily.core.QrCodeGenerator
import com.paligot.confily.core.extensions.encodeToString
import com.paligot.confily.core.fs.ConferenceFileSystem
import com.paligot.confily.core.kvalue.ConferenceSettings
import com.paligot.confily.core.networking.entities.ExportUsers
import com.paligot.confily.core.networking.entities.User
import com.paligot.confily.core.networking.entities.UserConfiguration
import com.paligot.confily.core.networking.entities.UserInfo
import com.paligot.confily.core.networking.entities.UserItem
import com.paligot.confily.core.networking.entities.mapToFs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat

class UserRepositoryImpl(
    private val userDao: UserDao,
    private val settings: ConferenceSettings,
    private val conferenceFs: ConferenceFileSystem,
    private val qrCodeGenerator: QrCodeGenerator
) : UserRepository {
    override fun fetchConfiguration(): Flow<UserConfiguration> = settings.fetchEventId()
        .flatMapConcat {
            combine(
                flow = userDao.fetchUser(eventId = it),
                flow2 = userDao.fetchCountUserScanned(eventId = it),
                transform = { user, count ->
                    UserConfiguration(
                        hasProfileCompleted = user?.qrCode != null,
                        countUsersScanned = count
                    )
                }
            )
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun fetchUserProfile(): Flow<User?> = settings.fetchEventId()
        .flatMapConcat {
            combine(
                flow = userDao.fetchUser(eventId = it),
                flow2 = userDao.fetchUserTicket(eventId = it),
                transform = { info, ticket ->
                    User(info = info, ticket = ticket)
                }
            )
        }

    override fun insertUserInfo(user: UserInfo) {
        userDao.insertUser(
            eventId = settings.getEventId(),
            user = user.copy(
                qrCode = qrCodeGenerator.generate(user.encodeToString())
            )
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun fetchUsersScanned(): Flow<List<UserItem>> = settings.fetchEventId()
        .flatMapConcat { userDao.fetchUsersScanned(eventId = it) }

    override fun insertUserScanned(user: UserItem): Boolean {
        val hasRequiredFields = user.email != "" && user.lastName != "" && user.firstName != ""
        if (!hasRequiredFields) return false
        userDao.insertUserScanned(eventId = settings.getEventId(), user = user)
        return true
    }

    override fun deleteUserScannedByEmail(email: String) = userDao
        .deleteUserByEmail(eventId = settings.getEventId(), email = email)

    override fun exportUserScanned(): ExportUsers {
        val eventId = settings.getEventId()
        return ExportUsers(
            mailto = userDao.getEmailProfile(eventId),
            filePath = conferenceFs.exportUsers(userDao.getUsersScanned(eventId).map { it.mapToFs() })
        )
    }
}
