package com.paligot.conferences.repositories

import com.paligot.conferences.Image
import com.paligot.conferences.database.UserDao
import com.paligot.conferences.network.ConferenceApi
import com.paligot.conferences.toNativeImage
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun fetchEmailQrCode(): Pair<String, Image>?
    suspend fun fetchEmailQrCode(email: String): Image
    suspend fun fetchNetworking(): Flow<List<String>>
    suspend fun insertEmailNetworking(email: String)

    object Factory {
        fun create(api: ConferenceApi, userDao: UserDao): UserRepository = UserRepositoryImpl(api, userDao)
    }
}

class UserRepositoryImpl(
    private val api: ConferenceApi,
    private val userDao: UserDao
) : UserRepository {
    override suspend fun fetchEmailQrCode(): Pair<String, Image>? {
        val email = userDao.fetchLastEmail() ?: return null
        val qrcode = userDao.fetchQrCode(email) ?: return null
        return email to qrcode.toNativeImage()
    }

    override suspend fun fetchEmailQrCode(email: String): Image {
        val qrcode = userDao.fetchQrCode(email)
        if (qrcode != null) return qrcode.toNativeImage()
        val qrcodeUrl = api.saveEmailQrCode(email)
        val image = api.fetchImage(qrcodeUrl.url)
        userDao.insertUser(email, image)
        return image.toNativeImage()
    }

    override suspend fun fetchNetworking(): Flow<List<String>> = userDao.fetchNetworking()
    override suspend fun insertEmailNetworking(email: String) = userDao.insertEmailNetworking(email)
}
