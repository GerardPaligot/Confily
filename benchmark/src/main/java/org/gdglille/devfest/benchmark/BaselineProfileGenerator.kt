package org.gdglille.devfest.benchmark

import androidx.benchmark.macro.ExperimentalBaselineProfilesApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalBaselineProfilesApi
@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {
    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generate() = rule.collectBaselineProfile("org.gdglille.devfest.android") {
        pressHome()
        startActivityAndWait()
        device.waitForIdle()
        listOf("Agenda", "Networking", "Partners", "Event").forEach {
            device.run {
                findObject(By.text(it)).click()
                waitForIdle()
            }
        }
    }
}
