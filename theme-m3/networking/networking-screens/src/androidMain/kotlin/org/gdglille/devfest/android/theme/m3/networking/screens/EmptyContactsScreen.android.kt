package org.gdglille.devfest.android.theme.m3.networking.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.theme.m3.networking.screens.EmptyContactsScreen

@Preview(showBackground = true)
@Composable
fun EmptyContactsScreenPreview() {
    Conferences4HallTheme {
        EmptyContactsScreen()
    }
}
