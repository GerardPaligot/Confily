package com.paligot.confily.baselineprofile.scenarios

import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.UiDevice
import kotlin.time.Duration.Companion.seconds
import com.paligot.confily.baselineprofile.runAction
import com.paligot.confily.baselineprofile.waitForObject

class AgendaPOM(private val device: UiDevice) {
    fun waitDataFetched() {
        device.waitForObject(TestScenario.Partners.tabName, 40.seconds)
    }

    fun open() {
        device.runAction(TestScenario.Agenda.tabName) { click() }
    }

    fun openFilters() {
        device.runAction(TestScenario.Agenda.filterAction) { click() }
    }

    fun openFirstItem() {
        device.runAction(TestScenario.Agenda.first) { click() }
    }

    fun scrollUpDown() {
        device.runAction(TestScenario.Agenda.list) {
            fling(Direction.DOWN)
            fling(Direction.UP)
        }
    }

    fun back() {
        device.runAction(TestScenario.back) { click() }
    }
}
