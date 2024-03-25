package org.gdglille.devfest.baselineprofile.scenarios

import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiDevice
import org.gdglille.devfest.baselineprofile.runAction

class PartnersPOM(private val device: UiDevice) {
    fun open() {
        device.runAction(DevfestScenario.Partners.tabName) { click() }
    }

    fun openFirstItem() {
        device.runAction(DevfestScenario.Partners.first) { click() }
    }

    fun scrollUpDown() {
        device.runAction(DevfestScenario.Partners.list) {
            fling(Direction.DOWN)
            fling(Direction.UP)
        }
    }
}
