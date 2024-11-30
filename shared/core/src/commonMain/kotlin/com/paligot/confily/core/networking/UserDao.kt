package com.paligot.confily.core.networking

import com.paligot.confily.core.networking.entities.UserInfo
import com.paligot.confily.core.networking.entities.UserItem
import com.paligot.confily.core.networking.entities.UserTicket
import kotlinx.coroutines.flow.Flow

interface UserDao {
    fun fetchUser(eventId: String): Flow<UserInfo?>
    fun fetchUserTicket(eventId: String): Flow<UserTicket?>
    fun fetchUsersScanned(eventId: String): Flow<List<UserItem>>
    fun getUsersScanned(eventId: String): List<UserItem>
    fun insertUser(eventId: String, user: UserInfo)
    fun insertUserScanned(eventId: String, user: UserItem)
    fun deleteUserByEmail(eventId: String, email: String)
    fun getEmailProfile(eventId: String): String?
}
