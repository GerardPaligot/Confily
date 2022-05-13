package org.gdglille.devfest.android.screens.scanner.vcard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import org.gdglille.devfest.android.R

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FeatureThatRequiresCameraPermission(
    navigateToSettingsScreen: () -> Unit,
    onRefusePermissionClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    PermissionRequired(
        permissionState = cameraPermissionState,
        permissionNotGrantedContent = {
            Column {
                Text(text = stringResource(id = R.string.text_camera_permission_explaination))
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                        Text(text = stringResource(id = R.string.action_submit_accept))
                    }
                    Button(onClick = onRefusePermissionClicked) {
                        Text(text = stringResource(id = R.string.action_submit_deny))
                    }
                }
            }
        },
        permissionNotAvailableContent = {
            Column {
                Text(text = stringResource(id = R.string.text_camera_permission_deny))
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = navigateToSettingsScreen) {
                    Text(text = stringResource(id = R.string.action_system_settings))
                }
            }
        },
        content = content
    )
}
