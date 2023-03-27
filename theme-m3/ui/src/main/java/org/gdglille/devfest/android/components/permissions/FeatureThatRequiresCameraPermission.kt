package org.gdglille.devfest.android.components.permissions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.R

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

@Composable
private fun FeatureThatRequiresCameraPermissionRequested(
    onAcceptPermissionClicked: () -> Unit,
    onRefusePermissionClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            style = MaterialTheme.typography.titleLarge,
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
            style = MaterialTheme.typography.titleLarge,
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
            onRefusePermissionClicked = {}
        )
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
