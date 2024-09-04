package org.gdglille.devfest.android.theme.m3.networking.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.models.ui.UserProfileUi
import com.paligot.confily.style.theme.Conferences4HallTheme

@Preview
@Composable
private fun MyProfilePreview() {
    Conferences4HallTheme {
        MyProfileScreen(
            profileUi = UserProfileUi.fake,
            onEditInformation = {}
        )
    }
}
