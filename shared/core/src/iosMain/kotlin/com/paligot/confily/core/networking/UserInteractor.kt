package com.paligot.confily.core.networking

import com.paligot.confily.core.networking.entities.mapToUserNetworkingUi
import com.paligot.confily.core.networking.entities.mapToUserProfileUi
import com.paligot.confily.networking.ui.models.UserNetworkingUi
import com.paligot.confily.networking.ui.models.UserProfileUi
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserInteractor(
    private val userRepository: UserRepository
) {
    @NativeCoroutines
    fun fetchUserProfile(): Flow<UserProfileUi?> = userRepository
        .fetchUserProfile()
        .map { it.mapToUserProfileUi() }

    @NativeCoroutines
    fun fetchUsersScanned(): Flow<ImmutableList<UserNetworkingUi>> = userRepository
        .fetchUsersScanned()
        .map { users -> users.map { it.mapToUserNetworkingUi() }.toImmutableList() }
}
