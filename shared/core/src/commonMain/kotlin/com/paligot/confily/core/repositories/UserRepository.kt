package com.paligot.confily.core.repositories

import com.paligot.confily.core.database.EventDao
import com.paligot.confily.core.database.UserDao
import com.paligot.confily.core.vcard.encodeToString
import com.paligot.confily.models.ui.ExportNetworkingUi
import com.paligot.confily.models.ui.Image
import com.paligot.confily.models.ui.UserNetworkingUi
import com.paligot.confily.models.ui.UserProfileUi
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat

interface UserRepository {
    @NativeCoroutines
    fun fetchProfile(): Flow<UserProfileUi?>
    fun saveProfile(email: String, firstName: String, lastName: String, company: String)

    @NativeCoroutines
    fun fetchNetworking(): Flow<ImmutableList<UserNetworkingUi>>
    fun insertNetworkingProfile(user: UserNetworkingUi): Boolean
    fun deleteNetworkProfile(email: String)
    fun exportNetworking(): ExportNetworkingUi

    object Factory {
        fun create(
            userDao: UserDao,
            eventDao: EventDao,
            qrCodeGenerator: QrCodeGenerator
        ): UserRepository = UserRepositoryImpl(userDao, eventDao, qrCodeGenerator)
    }
}

interface QrCodeGenerator {
    fun generate(text: String): Image
}

class UserRepositoryImpl(
    private val userDao: UserDao,
    private val eventDao: EventDao,
    private val qrCodeGenerator: QrCodeGenerator
) : UserRepository {
    override fun fetchProfile(): Flow<UserProfileUi?> = eventDao.fetchEventId()
        .flatMapConcat {
            combine(
                userDao.fetchProfile(eventId = it),
                userDao.fetchUserPreview(eventId = it),
                transform = { profile, preview ->
                    return@combine profile ?: preview
                }
            )
        }

    override fun saveProfile(email: String, firstName: String, lastName: String, company: String) {
        val qrCode = qrCodeGenerator.generate(
            UserNetworkingUi(email, firstName, lastName, company).encodeToString()
        )
        val profile = UserProfileUi(email, firstName, lastName, company, qrCode)
        userDao.insertUser(eventId = eventDao.getEventId(), user = profile)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun fetchNetworking(): Flow<ImmutableList<UserNetworkingUi>> = eventDao.fetchEventId()
        .flatMapConcat { userDao.fetchNetworking(eventId = it) }

    override fun insertNetworkingProfile(user: UserNetworkingUi): Boolean {
        val hasRequiredFields = user.email != "" && user.lastName != "" && user.firstName != ""
        if (!hasRequiredFields) return false
        userDao.insertEmailNetworking(eventId = eventDao.getEventId(), userNetworkingUi = user)
        return true
    }

    override fun deleteNetworkProfile(email: String) = userDao.deleteNetworking(
        eventId = eventDao.getEventId(),
        email = email
    )

    override fun exportNetworking(): ExportNetworkingUi = ExportNetworkingUi(
        mailto = userDao.getEmailProfile(eventDao.getEventId()),
        filePath = userDao.exportNetworking(eventId = eventDao.getEventId())
    )
}
