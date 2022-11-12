package org.gdglille.devfest.android.screens.networking

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.ui.R
import org.gdglille.devfest.models.UserProfileUi

@Composable
fun MyProfile(
    profileUi: UserProfileUi,
    modifier: Modifier = Modifier,
    onEditInformation: () -> Unit
) {
    val typography = MaterialTheme.typography.bodyMedium
    val color = MaterialTheme.colorScheme.onBackground
    val isInPreview = LocalInspectionMode.current
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 24.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!isInPreview && profileUi.qrCode != null) {
            item {
                Image(
                    bitmap = profileUi.qrCode!!.asImageBitmap(),
                    contentDescription = stringResource(id = R.string.semantic_profile_qrcode),
                    modifier = Modifier.size(196.dp)
                )
            }
        }
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${profileUi.firstName} ${profileUi.lastName}",
                    style = typography,
                    fontWeight = FontWeight.W700,
                    color = color
                )
                if (profileUi.company != "") {
                    Text(
                        text = profileUi.company,
                        style = typography,
                        color = color
                    )
                }
                Text(
                    text = profileUi.email,
                    style = typography,
                    color = color
                )
            }
        }
        item {
            Button(onClick = onEditInformation) {
                Text(text = stringResource(R.string.action_edit_profile))
            }
        }
    }
}

@Preview
@Composable
fun MyProfilePreview() {
    Conferences4HallTheme {
        MyProfile(
            profileUi = UserProfileUi.fake,
            onEditInformation = {}
        )
    }
}
