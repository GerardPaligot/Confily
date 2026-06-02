package com.paligot.confily.core

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DeviceIdProviderDesktop : DeviceIdProvider {
    @OptIn(ExperimentalUuidApi::class)
    override fun deviceId(): String = Uuid.random().toString()
}
