package com.paligot.confily.core

import platform.UIKit.UIDevice
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DeviceIdProviderIos : DeviceIdProvider {
    @OptIn(ExperimentalUuidApi::class)
    override fun deviceId(): String =
        UIDevice.currentDevice.identifierForVendor?.UUIDString ?: Uuid.random().toString()
}
