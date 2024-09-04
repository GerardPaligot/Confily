package com.paligot.confily.style.events.cards

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.style.theme.Conferences4HallTheme
import kotlinx.collections.immutable.persistentListOf

@Preview
@Composable
private fun AddressCardPreview() {
    Conferences4HallTheme {
        AddressCard(
            formattedAddress = persistentListOf(
                "Lille Grand Palais",
                "Bd des Cités Unies 1",
                "Lille"
            ),
            onItineraryClicked = {}
        )
    }
}
