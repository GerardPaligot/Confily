package com.paligot.confily.style.components.permissions

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FeatureThatRequiresCameraPermission(
    navigateToSettingsScreen: () -> Unit,
    onRefusePermissionClicked: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val paddingModifier = modifier.padding(24.dp)
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    when (cameraPermissionState.status) {
        is PermissionStatus.Denied -> {
            if (cameraPermissionState.status.shouldShowRationale) {
                FeatureThatRequiresCameraPermissionDenied(
                    modifier = paddingModifier,
                    navigateToSettingsScreen = navigateToSettingsScreen
                )
            } else {
                FeatureThatRequiresCameraPermissionRequested(
                    modifier = paddingModifier,
                    onAcceptPermissionClicked = { cameraPermissionState.launchPermissionRequest() },
                    onRefusePermissionClicked = onRefusePermissionClicked
                )
            }
        }

        is PermissionStatus.Granted -> {
            content()
        }
    }
}
