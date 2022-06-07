package org.gdglille.devfest.android.screens.scanner

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.*
import org.gdglille.devfest.android.R
import org.gdglille.devfest.android.theme.Conferences4HallTheme

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FeatureThatRequiresCameraPermission(
    navigateToSettingsScreen: () -> Unit,
    onRefusePermissionClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    val paddingModifier = Modifier.padding(24.dp)
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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun FeatureThatRequiresCameraPermissionRequested(
    modifier: Modifier = Modifier,
    onAcceptPermissionClicked: () -> Unit,
    onRefusePermissionClicked: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            style = MaterialTheme.typography.h6,
            text = stringResource(id = R.string.text_permission)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(id = R.string.text_camera_permission_explaination))
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onRefusePermissionClicked) {
                Text(text = stringResource(id = R.string.action_submit_later))
            }
            Button(onClick = onAcceptPermissionClicked) {
                Text(text = stringResource(id = R.string.action_submit_accept))
            }
        }
    }
}

@Composable
private fun FeatureThatRequiresCameraPermissionDenied(
    modifier: Modifier = Modifier,
    navigateToSettingsScreen: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            style = MaterialTheme.typography.h6,
            text = stringResource(id = R.string.text_permission)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(id = R.string.text_camera_permission_deny))
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = navigateToSettingsScreen) {
            Text(text = stringResource(id = R.string.action_system_settings))
        }
    }
}

@Preview
@Composable
private fun FeatureThatRequiresCameraPermissionRequestedPreview() {
    Conferences4HallTheme {
        FeatureThatRequiresCameraPermissionRequested(
            onAcceptPermissionClicked = {},
            onRefusePermissionClicked = {})
    }
}

@Preview
@Composable
private fun FeatureThatRequiresCameraPermissionDeniedPreview() {
    Conferences4HallTheme {
        FeatureThatRequiresCameraPermissionDenied(
            navigateToSettingsScreen = {}
        )
    }
}