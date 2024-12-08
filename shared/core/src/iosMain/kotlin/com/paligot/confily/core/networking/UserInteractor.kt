package com.paligot.confily.core.networking

import com.paligot.confily.core.networking.entities.mapToUserNetworkingUi
import com.paligot.confily.core.networking.entities.mapToUserProfileUi
import com.paligot.confily.models.ui.UserNetworkingUi
import com.paligot.confily.models.ui.UserProfileUi
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserInteractor(
    private val userRepository: UserRepository
) {
    @NativeCoroutines
    fun fetchUserProfile(): Flow<UserProfileUi> = userRepository
        .fetchUserProfile()
        .map {
            it?.mapToUserProfileUi() ?: UserProfileUi(
                email = "",
                firstName = "",
                lastName = "",
                company = "",
                qrCode = null
            )
        }

    @NativeCoroutines
    fun fetchUsersScanned(): Flow<ImmutableList<UserNetworkingUi>> = userRepository
        .fetchUsersScanned()
        .map { users -> users.map { it.mapToUserNetworkingUi() }.toImmutableList() }
}
