package com.paligot.confily.core.quiz

import com.paligot.confily.core.DeviceIdProvider

class FakeDeviceIdProvider(private val id: String = "device-1") : DeviceIdProvider {
    var callCount: Int = 0
        private set

    override fun deviceId(): String {
        callCount++
        return id
    }
}
