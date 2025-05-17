package com.paligot.confily.core.networking

import com.paligot.confily.core.QrCodeGenerator
import com.paligot.confily.core.fs.ConferenceFileSystem
import com.paligot.confily.core.kvalue.ConferenceSettings
import com.paligot.confily.core.networking.entities.ExportUsers
import com.paligot.confily.core.networking.entities.User
import com.paligot.confily.core.networking.entities.UserConfiguration
import com.paligot.confily.core.networking.entities.UserInfo
import com.paligot.confily.core.networking.entities.UserItem
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun fetchConfiguration(): Flow<UserConfiguration>
    fun fetchUserProfile(): Flow<User>
    fun insertUserInfo(user: UserInfo)
    fun fetchUsersScanned(): Flow<List<UserItem>>
    fun insertUserScanned(user: UserItem): Boolean
    fun deleteUserScannedByEmail(email: String)
    fun exportUserScanned(): ExportUsers

    object Factory {
        fun create(
            userDao: UserDao,
            settings: ConferenceSettings,
            conferenceFs: ConferenceFileSystem,
            qrCodeGenerator: QrCodeGenerator
        ): UserRepository = UserRepositoryImpl(userDao, settings, conferenceFs, qrCodeGenerator)
    }
}
