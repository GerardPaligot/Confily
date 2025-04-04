package com.paligot.confily.baselineprofile.scenarios

import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiDevice
import com.paligot.confily.baselineprofile.runAction
import com.paligot.confily.baselineprofile.waitForObject

class PartnersPOM(private val device: UiDevice) {
    fun open() {
        device.runAction(TestScenario.Partners.tabName) { click() }
    }

    fun openFirstItem() {
        device.runAction(TestScenario.Partners.first) { click() }
        device.waitForObject(TestScenario.Partners.detailTitle)
    }

    fun scrollUpDown() {
        device.runAction(TestScenario.Partners.list) {
            fling(Direction.DOWN)
            fling(Direction.UP)
        }
    }

    fun back() {
        device.runAction(TestScenario.back) { click() }
    }
}
