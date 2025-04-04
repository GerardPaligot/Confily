package com.paligot.confily.baselineprofile.scenarios

import androidx.test.uiautomator.UiDevice
import com.paligot.confily.baselineprofile.runAction

class InfoPOM(private val device: UiDevice) {
    fun open() {
        device.runAction(TestScenario.Info.tabName) { click() }
    }

    fun expandOverflowMenu() {
        device.runAction(TestScenario.Info.overflowAction) { click() }
    }

    fun changeEventAction() {
        device.runAction(TestScenario.Info.changeEventAction) { click() }
    }
}
