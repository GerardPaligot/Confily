package com.paligot.confily.core

interface DeviceIdProvider {
    /** Returns a non-blank, best-effort stable identifier for this device/install. */
    fun deviceId(): String
}
