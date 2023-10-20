package org.gdglille.devfest.android.theme.m3.style.permissions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.R

@Composable
internal fun FeatureThatRequiresCameraPermissionRequested(
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
