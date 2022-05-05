package org.gdglille.devfest.repositories

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.gdglille.devfest.Image
import org.gdglille.devfest.database.UserDao
import org.gdglille.devfest.models.UserNetworkingUi
import org.gdglille.devfest.models.UserProfileUi
import org.gdglille.devfest.vcard.encodeToString

interface UserRepository {
    suspend fun fetchProfile(): UserProfileUi?
    suspend fun saveProfile(email: String, firstName: String, lastName: String, company: String): UserProfileUi
    suspend fun fetchNetworking(): Flow<List<UserNetworkingUi>>
    suspend fun insertNetworkingProfile(user: UserNetworkingUi)
    suspend fun deleteNetworkProfile(email: String)

    // Kotlin/Native client
    fun startCollectNetworking(success: (List<UserNetworkingUi>) -> Unit)
    fun stopCollectNetworking()

    object Factory {
        fun create(userDao: UserDao, qrCodeGenerator: QrCodeGenerator): UserRepository = UserRepositoryImpl(userDao, qrCodeGenerator)
    }
}

interface QrCodeGenerator {
    fun generate(text: String): Image
}

class UserRepositoryImpl(
    private val userDao: UserDao,
    private val qrCodeGenerator: QrCodeGenerator
) : UserRepository {
    override suspend fun fetchProfile(): UserProfileUi? {
        val email = userDao.fetchLastEmail() ?: return null
        return userDao.fetchUser(email)
    }

    override suspend fun saveProfile(email: String, firstName: String, lastName: String, company: String): UserProfileUi {
        val qrCode = qrCodeGenerator.generate(UserNetworkingUi(email, firstName, lastName, company).encodeToString())
        val profile = UserProfileUi(email, firstName, lastName, company, qrCode)
        userDao.insertUser(profile)
        return profile
    }

    override suspend fun fetchNetworking(): Flow<List<UserNetworkingUi>> = userDao.fetchNetworking()
    override suspend fun insertNetworkingProfile(user: UserNetworkingUi) =
        userDao.insertEmailNetworking(user)
    override suspend fun deleteNetworkProfile(email: String) = userDao.deleteNetworking(email)

    private val coroutineScope: CoroutineScope = MainScope()
    var agendaJob: Job? = null
    override fun startCollectNetworking(success: (List<UserNetworkingUi>) -> Unit) {
        agendaJob = coroutineScope.launch {
            fetchNetworking().collect {
                success(it)
            }
        }
    }

    override fun stopCollectNetworking() {
        agendaJob?.cancel()
    }
}
