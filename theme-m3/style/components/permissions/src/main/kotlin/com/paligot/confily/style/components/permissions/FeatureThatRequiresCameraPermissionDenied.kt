package com.paligot.confily.style.components.permissions

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
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.action_system_settings
import com.paligot.confily.resources.text_camera_permission_deny
import com.paligot.confily.resources.text_permission
import com.paligot.confily.style.theme.Conferences4HallTheme
import org.jetbrains.compose.resources.stringResource

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
