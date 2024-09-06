package com.paligot.confily.baselineprofile.scenarios

import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiDevice
import com.paligot.confily.baselineprofile.runAction

class SpeakersPOM(private val device: UiDevice) {
    fun open() {
        device.runAction(DevfestScenario.Speakers.tabName) { click() }
    }

    fun openFirstItem() {
        device.runAction(DevfestScenario.Speakers.first) { click() }
    }

    fun scrollUpDown() {
        device.runAction(DevfestScenario.Speakers.list) {
            fling(Direction.DOWN)
            fling(Direction.UP)
        }
    }

    fun back() {
        device.runAction(DevfestScenario.back) { click() }
    }
}
