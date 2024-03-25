package org.gdglille.devfest.baselineprofile

import android.os.SystemClock
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.SearchCondition
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

fun UiDevice.waitForObject(selector: BySelector, timeout: Duration = 5.seconds): UiObject2 {
    if (wait(Until.hasObject(selector), timeout)) {
        return findObject(selector)
    }
    error("Object with selector [$selector] not found")
}

fun <R> UiDevice.wait(condition: SearchCondition<R>, timeout: Duration): R {
    return wait(condition, timeout.inWholeMilliseconds)
}

fun UiDevice.runAction(
    selector: BySelector,
    maxRetries: Int = 6,
    action: UiObject2.() -> Unit,
) {
    waitForObject(selector)

    retry(maxRetries = maxRetries, delay = 1.seconds) {
        // Wait for idle, to avoid recompositions causing StaleObjectExceptions
        waitForIdle()

        requireNotNull(findObject(selector)).action()
    }
}

private fun retry(maxRetries: Int, delay: Duration, block: () -> Unit) {
    repeat(maxRetries) { run ->
        val result = runCatching { block() }
        if (result.isSuccess) {
            return
        }
        if (run == maxRetries - 1) {
            result.getOrThrow()
        } else {
            SystemClock.sleep(delay.inWholeMilliseconds)
        }
    }
}
