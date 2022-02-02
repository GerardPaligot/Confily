package com.paligot.conferences.backend.users

import com.paligot.conferences.backend.storage.Storage

class UserDao(private val storage: Storage) {
    private val qrCodeGenerator = QrCodeGenerator()

    suspend fun saveQrCode(eventId: String, email: String) = storage.upload(
        filename = "$eventId/qrcodes/$email/qrcode.png",
        content = qrCodeGenerator.getQrCodeBytes(email)
    )
}
