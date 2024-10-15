package com.paligot.confily.core.networking

import com.paligot.confily.models.ui.UserNetworkingUi
import com.paligot.confily.models.ui.UserProfileUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface UserDao {
    fun fetchProfile(eventId: String): Flow<UserProfileUi?>
    fun fetchUserPreview(eventId: String): Flow<UserProfileUi?>
    fun fetchNetworking(eventId: String): Flow<ImmutableList<UserNetworkingUi>>
    fun getUsers(eventId: String): ImmutableList<UserNetworkingUi>
    fun insertUser(
        eventId: String,
        email: String,
        firstName: String,
        lastName: String,
        company: String?,
        qrCode: ByteArray
    )

    fun insertEmailNetworking(eventId: String, userNetworkingUi: UserNetworkingUi)
    fun deleteNetworking(eventId: String, email: String)
    fun getEmailProfile(eventId: String): String?
}
