package org.gdglille.devfest.repositories

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.gdglille.devfest.Image
import org.gdglille.devfest.database.UserDao
import org.gdglille.devfest.models.NetworkingUi
import org.gdglille.devfest.models.UserNetworkingUi
import org.gdglille.devfest.models.UserProfileUi
import org.gdglille.devfest.vcard.encodeToString

interface UserRepository {
    suspend fun fetchProfile(): Flow<UserProfileUi?>
    suspend fun saveProfile(email: String, firstName: String, lastName: String, company: String)
    suspend fun fetchNetworking(): Flow<List<UserNetworkingUi>>
    suspend fun insertNetworkingProfile(user: UserNetworkingUi): Boolean
    suspend fun deleteNetworkProfile(email: String)

    // Kotlin/Native client
    fun startCollectNetworking(success: (NetworkingUi) -> Unit)
    fun stopCollectNetworking()

    object Factory {
        fun create(userDao: UserDao, qrCodeGenerator: QrCodeGenerator): UserRepository =
            UserRepositoryImpl(userDao, qrCodeGenerator)
    }
}

interface QrCodeGenerator {
    fun generate(text: String): Image
}

class UserRepositoryImpl(
    private val userDao: UserDao,
    private val qrCodeGenerator: QrCodeGenerator
) : UserRepository {
    override suspend fun fetchProfile(): Flow<UserProfileUi?> = combine(
        userDao.fetchProfile(),
        userDao.fetchUserPreview(),
        transform = { profile, preview ->
            return@combine profile ?: preview
        }
    )

    override suspend fun saveProfile(email: String, firstName: String, lastName: String, company: String) {
        val qrCode = qrCodeGenerator.generate(UserNetworkingUi(email, firstName, lastName, company).encodeToString())
        val profile = UserProfileUi(email, firstName, lastName, company, qrCode)
        userDao.insertUser(profile)
    }

    override suspend fun fetchNetworking(): Flow<List<UserNetworkingUi>> = userDao.fetchNetworking()
    override suspend fun insertNetworkingProfile(user: UserNetworkingUi): Boolean {
        val hasRequiredFields = user.email != "" && user.lastName != "" && user.firstName != ""
        if (!hasRequiredFields) return false
        userDao.insertEmailNetworking(user)
        return true
    }

    override suspend fun deleteNetworkProfile(email: String) = userDao.deleteNetworking(email)

    private val coroutineScope: CoroutineScope = MainScope()
    private var networkingJob: Job? = null
    override fun startCollectNetworking(success: (NetworkingUi) -> Unit) {
        networkingJob = coroutineScope.launch {
            combine(
                fetchProfile(),
                fetchNetworking(),
                transform = { profileUi, contacts ->
                    NetworkingUi(
                        userProfileUi = profileUi ?: UserProfileUi(
                            email = "",
                            firstName = "",
                            lastName = "",
                            company = "",
                            qrCode = null
                        ),
                        showQrCode = false,
                        users = contacts
                    )
                }
            ).collect {
                success(it)
            }
        }
    }

    override fun stopCollectNetworking() {
        networkingJob?.cancel()
    }
}
