package com.paligot.confily.baselineprofile.scenarios

import androidx.test.uiautomator.UiDevice
import com.paligot.confily.baselineprofile.runAction

class EventListPOM(private val device: UiDevice) {
    fun goToPastEvents() {
        device.runAction(TestScenario.Event.pastEvents) { click() }
    }

    fun clickDevfest() {
        device.runAction(TestScenario.Event.name) { click() }
    }
}
