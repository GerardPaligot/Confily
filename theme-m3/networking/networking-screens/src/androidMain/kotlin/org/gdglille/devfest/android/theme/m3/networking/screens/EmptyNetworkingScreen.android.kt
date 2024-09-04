package org.gdglille.devfest.android.theme.m3.networking.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.style.theme.Conferences4HallTheme
import org.gdglille.devfest.theme.m3.networking.screens.EmptyNetworkingScreen

@Preview(showBackground = true)
@Composable
private fun EmptyNetworkingScreenPreview() {
    Conferences4HallTheme {
        EmptyNetworkingScreen()
    }
}
