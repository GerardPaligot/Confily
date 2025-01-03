package com.paligot.confily.networking.panes

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.networking.ui.models.NetworkingUi
import com.paligot.confily.style.theme.ConfilyTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Preview
@Composable
private fun ContactsPreview() {
    ConfilyTheme {
        Scaffold {
            ContactsScreen(
                users = NetworkingUi.fake.users,
                onNetworkDeleted = {}
            )
        }
    }
}
