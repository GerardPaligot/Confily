package org.gdglille.devfest.baselineprofile.scenarios

import androidx.test.uiautomator.UiDevice
import org.gdglille.devfest.baselineprofile.runAction

class InfoPOM(private val device: UiDevice) {
    fun open() {
        device.runAction(DevfestScenario.Info.tabName) { click() }
    }

    fun expandOverflowMenu() {
        device.runAction(DevfestScenario.Info.overflowAction) { click() }
    }

    fun changeEventAction() {
        device.runAction(DevfestScenario.Info.changeEventAction) { click() }
    }
}
