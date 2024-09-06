package com.paligot.confily.baselineprofile.scenarios

import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiDevice
import kotlin.time.Duration.Companion.seconds
import com.paligot.confily.baselineprofile.runAction
import com.paligot.confily.baselineprofile.waitForObject

class AgendaPOM(private val device: UiDevice) {
    fun waitDataFetched() {
        device.waitForObject(DevfestScenario.Partners.tabName, 20.seconds)
    }

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
