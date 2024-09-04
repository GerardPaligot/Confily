package com.paligot.confily.partners.panes

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.models.ui.PartnerGroupsUi
import com.paligot.confily.style.theme.Conferences4HallTheme

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
