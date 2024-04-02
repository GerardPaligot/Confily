package org.gdglille.devfest.android.theme.m3.style.permissions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.shared.resources.Resource
import org.gdglille.devfest.android.shared.resources.action_system_settings
import org.gdglille.devfest.android.shared.resources.text_camera_permission_deny
import org.gdglille.devfest.android.shared.resources.text_permission
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun FeatureThatRequiresCameraPermissionDenied(
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
            text = stringResource(Resource.string.text_permission)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(Resource.string.text_camera_permission_deny))
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = navigateToSettingsScreen) {
            Text(text = stringResource(Resource.string.action_system_settings))
        }
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
