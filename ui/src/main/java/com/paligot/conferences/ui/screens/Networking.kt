package com.paligot.conferences.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paligot.conferences.repositories.UserProfileUi
import com.paligot.conferences.ui.components.users.EmailInput
import com.paligot.conferences.ui.components.users.EmailItem
import com.paligot.conferences.ui.components.users.QrCodeDialog
import com.paligot.conferences.ui.theme.Conferences4HallTheme

@Composable
fun Networking(
    profileUi: UserProfileUi,
    modifier: Modifier = Modifier,
    onValueChanged: (String) -> Unit,
    onValidation: () -> Unit,
    onQrCodeClicked: () -> Unit,
    onDismissRequest: () -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            EmailInput(
                value = profileUi.email,
                hasQrCode = profileUi.hasQrCode,
                modifier = Modifier.padding(horizontal = 8.dp),
                onValueChanged = onValueChanged,
                onValidation = onValidation,
                onQrCodeClicked = onQrCodeClicked
            )
        }
        itemsIndexed(profileUi.emails) { index, email ->
            EmailItem(email = email)
            if (index < profileUi.emails.size - 1) {
                Divider()
            }
        }
    }
    if (profileUi.showQrCode && profileUi.qrcode != null) {
        QrCodeDialog(
            email = profileUi.email,
            imageBitmap = profileUi.qrcode!!.asImageBitmap(),
            onDismissRequest = onDismissRequest
        )
    }
}

val fakeProfileUi = UserProfileUi(
    email = "email@fake.com",
    hasQrCode = true,
    showQrCode = false,
    emails = arrayListOf(
        "gerard@gdglille.org",
        "gerard@gdglille.org",
        "gerard@gdglille.org",
        "gerard@gdglille.org",
        "gerard@gdglille.org",
    ),
    qrcode = null
)

@Preview
@Composable
fun NetworkingPreview() {
    Conferences4HallTheme {
        Scaffold {
            Networking(
                profileUi = fakeProfileUi,
                onValueChanged = {},
                onValidation = {},
                onQrCodeClicked = {},
                onDismissRequest = {}
            )
        }
    }
}
