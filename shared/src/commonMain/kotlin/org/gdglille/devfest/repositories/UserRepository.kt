package org.gdglille.devfest.repositories

import com.rickclephas.kmp.nativecoroutines.NativeCoroutineScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.gdglille.devfest.Image
import org.gdglille.devfest.database.EventDao
import org.gdglille.devfest.database.UserDao
import org.gdglille.devfest.models.ExportNetworkingUi
import org.gdglille.devfest.models.UserNetworkingUi
import org.gdglille.devfest.models.UserProfileUi
import org.gdglille.devfest.vcard.encodeToString

interface UserRepository {
    fun fetchProfile(): Flow<UserProfileUi?>
    fun saveProfile(email: String, firstName: String, lastName: String, company: String)
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
    @NativeCoroutineScope
    private val coroutineScope: CoroutineScope = MainScope()

    override fun fetchProfile(): Flow<UserProfileUi?> = combine(
        userDao.fetchProfile(eventId = eventDao.fetchEventId()),
        userDao.fetchUserPreview(eventId = eventDao.fetchEventId()),
        transform = { profile, preview ->
            return@combine profile ?: preview
        }
    )

    override fun saveProfile(email: String, firstName: String, lastName: String, company: String) {
        val qrCode = qrCodeGenerator.generate(
            UserNetworkingUi(email, firstName, lastName, company).encodeToString()
        )
        val profile = UserProfileUi(email, firstName, lastName, company, qrCode)
        userDao.insertUser(eventId = eventDao.fetchEventId(), user = profile)
    }

    override fun fetchNetworking(): Flow<ImmutableList<UserNetworkingUi>> = userDao.fetchNetworking(
        eventId = eventDao.fetchEventId()
    )

    override fun insertNetworkingProfile(user: UserNetworkingUi): Boolean {
        val hasRequiredFields = user.email != "" && user.lastName != "" && user.firstName != ""
        if (!hasRequiredFields) return false
        userDao.insertEmailNetworking(eventId = eventDao.fetchEventId(), userNetworkingUi = user)
        return true
    }

    override fun deleteNetworkProfile(email: String) = userDao.deleteNetworking(
        eventId = eventDao.fetchEventId(),
        email = email
    )

    override fun exportNetworking(): ExportNetworkingUi = ExportNetworkingUi(
        mailto = userDao.getEmailProfile(eventDao.fetchEventId()),
        filePath = userDao.exportNetworking(eventId = eventDao.fetchEventId())
    )
}
