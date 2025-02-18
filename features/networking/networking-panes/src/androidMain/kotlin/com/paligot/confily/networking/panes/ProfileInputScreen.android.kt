package com.paligot.confily.networking.panes

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.networking.ui.models.UserProfileUi
import com.paligot.confily.style.theme.ConfilyTheme

@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
private fun ProfileInputScreenPreview() {
    ConfilyTheme {
        Scaffold {
            ProfileInputScreen(
                profile = UserProfileUi.fake,
                onValueChanged = { _, _ -> },
                onValidation = {},
                onBackClicked = {}
            )
        }
    }
}
