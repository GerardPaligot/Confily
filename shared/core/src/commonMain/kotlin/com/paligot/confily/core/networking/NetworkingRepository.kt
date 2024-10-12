package com.paligot.confily.core.networking

import com.paligot.confily.core.QrCodeGenerator
import com.paligot.confily.core.kvalue.ConferenceSettings
import com.paligot.confily.core.extensions.encodeToString
import com.paligot.confily.core.fs.ConferenceFileSystem
import com.paligot.confily.models.ui.ExportNetworkingUi
import com.paligot.confily.models.ui.UserNetworkingUi
import com.paligot.confily.models.ui.UserProfileUi
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat

interface NetworkingRepository {
    @NativeCoroutines
    fun fetchProfile(): Flow<UserProfileUi?>

    @NativeCoroutines
    fun fetchNetworking(): Flow<ImmutableList<UserNetworkingUi>>

    fun saveProfile(email: String, firstName: String, lastName: String, company: String)
    fun insertNetworkingProfile(user: UserNetworkingUi): Boolean
    fun deleteNetworkProfile(email: String)
    fun exportNetworking(): ExportNetworkingUi

    object Factory {
        fun create(
            userDao: UserDao,
            settings: ConferenceSettings,
            conferenceFs: ConferenceFileSystem,
            qrCodeGenerator: QrCodeGenerator
        ): NetworkingRepository =
            NetworkingRepositoryImpl(userDao, settings, conferenceFs, qrCodeGenerator)
    }
}

class NetworkingRepositoryImpl(
    private val userDao: UserDao,
    private val settings: ConferenceSettings,
    private val conferenceFs: ConferenceFileSystem,
    private val qrCodeGenerator: QrCodeGenerator
) : NetworkingRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun fetchProfile(): Flow<UserProfileUi?> = settings.fetchEventId()
        .flatMapConcat {
            combine(
                userDao.fetchProfile(eventId = it),
                userDao.fetchUserPreview(eventId = it),
                transform = { profile, preview ->
                    return@combine profile ?: preview
                }
            )
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun fetchNetworking(): Flow<ImmutableList<UserNetworkingUi>> = settings.fetchEventId()
        .flatMapConcat { userDao.fetchNetworking(eventId = it) }

    override fun saveProfile(email: String, firstName: String, lastName: String, company: String) {
        val qrCode = qrCodeGenerator.generate(
            UserNetworkingUi(email, firstName, lastName, company).encodeToString()
        )
        userDao.insertUser(
            eventId = settings.getEventId(),
            email = email,
            firstName = firstName,
            lastName = lastName,
            company = company,
            qrCode = qrCode
        )
    }

    override fun insertNetworkingProfile(user: UserNetworkingUi): Boolean {
        val hasRequiredFields = user.email != "" && user.lastName != "" && user.firstName != ""
        if (!hasRequiredFields) return false
        userDao.insertEmailNetworking(eventId = settings.getEventId(), userNetworkingUi = user)
        return true
    }

    override fun deleteNetworkProfile(email: String) = userDao.deleteNetworking(
        eventId = settings.getEventId(),
        email = email
    )

    override fun exportNetworking(): ExportNetworkingUi {
        val eventId = settings.getEventId()
        return ExportNetworkingUi(
            mailto = userDao.getEmailProfile(eventId),
            filePath = conferenceFs.exportUsers(userDao.getUsers(eventId).map { it.toUser() })
        )
    }
}
