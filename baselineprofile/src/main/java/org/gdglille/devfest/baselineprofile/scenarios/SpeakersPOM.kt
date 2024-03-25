package org.gdglille.devfest.baselineprofile.scenarios

import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiDevice
import org.gdglille.devfest.baselineprofile.runAction

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
}
