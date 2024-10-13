package com.paligot.confily.core.networking

import com.paligot.confily.models.ui.UserNetworkingUi
import com.paligot.confily.models.ui.UserProfileUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

class UserDaoSettings : UserDao {
    override fun fetchProfile(eventId: String): Flow<UserProfileUi?> {
        TODO("Not yet implemented")
    }

    override fun fetchUserPreview(eventId: String): Flow<UserProfileUi?> {
        TODO("Not yet implemented")
    }

    override fun fetchNetworking(eventId: String): Flow<ImmutableList<UserNetworkingUi>> {
        TODO("Not yet implemented")
    }

    override fun getUsers(eventId: String): ImmutableList<UserNetworkingUi> {
        TODO("Not yet implemented")
    }

    override fun insertUser(
        eventId: String,
        email: String,
        firstName: String,
        lastName: String,
        company: String?,
        qrCode: ByteArray
    ) {
        TODO("Not yet implemented")
    }

    override fun insertEmailNetworking(eventId: String, userNetworkingUi: UserNetworkingUi) {
        TODO("Not yet implemented")
    }

    override fun deleteNetworking(eventId: String, email: String) {
        TODO("Not yet implemented")
    }

    override fun getEmailProfile(eventId: String): String? {
        TODO("Not yet implemented")
    }
}
