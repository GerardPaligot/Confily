package com.paligot.confily.baselineprofile.scenarios

import androidx.test.uiautomator.UiDevice
import com.paligot.confily.baselineprofile.runAction

class NetworkingPOM(private val device: UiDevice) {
    fun open() {
        device.runAction(TestScenario.Networking.tabName) { click() }
    }
}
