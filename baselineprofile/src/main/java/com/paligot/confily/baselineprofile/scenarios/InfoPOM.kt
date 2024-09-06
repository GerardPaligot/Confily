package com.paligot.confily.baselineprofile.scenarios

import androidx.test.uiautomator.UiDevice
import com.paligot.confily.baselineprofile.runAction

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
