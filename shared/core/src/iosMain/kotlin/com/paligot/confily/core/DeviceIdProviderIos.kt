package com.paligot.confily.core

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import platform.UIKit.UIDevice

class DeviceIdProviderIos : DeviceIdProvider {
    @OptIn(ExperimentalUuidApi::class)
    override fun deviceId(): String =
        UIDevice.currentDevice.identifierForVendor?.UUIDString ?: Uuid.random().toString()
}
