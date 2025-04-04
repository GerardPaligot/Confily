package com.paligot.confily.baselineprofile.scenarios

import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiDevice
import com.paligot.confily.baselineprofile.runAction

class SpeakersPOM(private val device: UiDevice) {
    fun open() {
        device.runAction(TestScenario.Speakers.tabName) { click() }
    }

    fun openFirstItem() {
        device.runAction(TestScenario.Speakers.first) { click() }
    }

    fun scrollUpDown() {
        device.runAction(TestScenario.Speakers.list) {
            fling(Direction.DOWN)
            fling(Direction.UP)
        }
    }

    fun back() {
        device.runAction(TestScenario.back) { click() }
    }
}
