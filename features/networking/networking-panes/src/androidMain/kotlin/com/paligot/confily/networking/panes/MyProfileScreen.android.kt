package com.paligot.confily.networking.panes

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.models.ui.UserProfileUi
import com.paligot.confily.style.theme.ConfilyTheme

@Preview
@Composable
private fun MyProfilePreview() {
    ConfilyTheme {
        MyProfileScreen(
            profileUi = UserProfileUi.fake,
            onEditInformation = {}
        )
    }
}
