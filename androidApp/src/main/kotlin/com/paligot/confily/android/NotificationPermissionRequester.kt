package com.paligot.confily.android

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.paligot.confily.core.NotificationPermissionRequester

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberNotificationPermissionRequester(): NotificationPermissionRequester {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        return NotificationPermissionRequester.Noop
    }
    val permissionState = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    return remember(permissionState) {
        NotificationPermissionRequester {
            if (!permissionState.status.isGranted) {
                permissionState.launchPermissionRequest()
            }
        }
    }
}
