package com.paligot.confily.core.networking

import com.paligot.confily.core.networking.entities.UserInfo
import com.paligot.confily.core.networking.entities.UserItem
import com.paligot.confily.core.networking.entities.UserTicket
import kotlinx.coroutines.flow.Flow

class UserDaoSettings : UserDao {
    override fun fetchUser(eventId: String): Flow<UserInfo?> {
        TODO("Not yet implemented")
    }

    override fun fetchUserTicket(eventId: String): Flow<UserTicket?> {
        TODO("Not yet implemented")
    }

    override fun fetchUsersScanned(eventId: String): Flow<List<UserItem>> {
        TODO("Not yet implemented")
    }

    override fun getUsersScanned(eventId: String): List<UserItem> {
        TODO("Not yet implemented")
    }

    override fun insertUser(eventId: String, user: UserInfo) {
        TODO("Not yet implemented")
    }

    override fun insertUserScanned(eventId: String, user: UserItem) {
        TODO("Not yet implemented")
    }

    override fun deleteUserByEmail(eventId: String, email: String) {
        TODO("Not yet implemented")
    }

    override fun getEmailProfile(eventId: String): String? {
        TODO("Not yet implemented")
    }
}
