package com.paligot.confily.core

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

fun interface NotificationPermissionRequester {
    fun request()

    companion object {
        val Noop: NotificationPermissionRequester = NotificationPermissionRequester { }
    }
}

val LocalNotificationPermissionRequester: ProvidableCompositionLocal<NotificationPermissionRequester> =
    staticCompositionLocalOf { NotificationPermissionRequester.Noop }
