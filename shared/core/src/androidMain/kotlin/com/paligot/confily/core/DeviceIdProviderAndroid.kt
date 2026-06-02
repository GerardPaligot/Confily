package com.paligot.confily.core

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DeviceIdProviderAndroid(private val context: Context) : DeviceIdProvider {
    @OptIn(ExperimentalUuidApi::class)
    @SuppressLint("HardwareIds")
    override fun deviceId(): String {
        val androidId = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        return if (androidId.isNullOrBlank()) Uuid.random().toString() else androidId
    }
}
