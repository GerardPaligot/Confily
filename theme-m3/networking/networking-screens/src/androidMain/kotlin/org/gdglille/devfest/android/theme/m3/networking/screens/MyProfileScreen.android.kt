package org.gdglille.devfest.android.theme.m3.networking.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.models.ui.UserProfileUi

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
