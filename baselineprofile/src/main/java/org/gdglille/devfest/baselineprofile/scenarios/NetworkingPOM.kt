package org.gdglille.devfest.baselineprofile.scenarios

import androidx.test.uiautomator.UiDevice
import org.gdglille.devfest.baselineprofile.runAction

class NetworkingPOM(private val device: UiDevice) {
    fun open() {
        device.runAction(DevfestScenario.Networking.tabName) { click() }
    }
}
