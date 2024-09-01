package org.gdglille.devfest.android.theme.m3.partners.screens

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.models.ui.PartnerGroupsUi
import org.gdglille.devfest.theme.m3.partners.screens.PartnersGridScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
private fun PartnersGridScreenPreview() {
    Conferences4HallTheme {
        PartnersGridScreen(
            partners = PartnerGroupsUi.fake,
            onPartnerClick = {}
        )
    }
}
