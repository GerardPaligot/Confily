package org.gdglille.devfest.baselineprofile.scenarios

import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiDevice
import org.gdglille.devfest.baselineprofile.runAction

class AgendaPOM(private val device: UiDevice) {
    fun open() {
        device.runAction(DevfestScenario.Agenda.tabName) { click() }
    }

    fun openFilters() {
        device.runAction(DevfestScenario.Agenda.filterAction) { click() }
    }

    fun openFirstItem() {
        device.runAction(DevfestScenario.Agenda.first) { click() }
    }

    fun scrollUpDown() {
        device.runAction(DevfestScenario.Agenda.list) {
            fling(Direction.DOWN)
            fling(Direction.UP)
        }
    }

    fun back() {
        device.runAction(DevfestScenario.back) { click() }
    }
}
