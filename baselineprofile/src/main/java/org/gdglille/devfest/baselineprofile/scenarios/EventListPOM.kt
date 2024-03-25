package org.gdglille.devfest.baselineprofile.scenarios

import androidx.test.uiautomator.UiDevice
import org.gdglille.devfest.baselineprofile.runAction

class EventListPOM(private val device: UiDevice) {
    fun goToPastEvents() {
        device.runAction(DevfestScenario.Event.pastEvents) { click() }
    }

    fun clickDevfest() {
        device.runAction(DevfestScenario.Event.name) { click() }
    }
}
