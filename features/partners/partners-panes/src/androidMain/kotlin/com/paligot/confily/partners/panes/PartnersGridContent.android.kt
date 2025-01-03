package com.paligot.confily.partners.panes

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.partners.ui.models.PartnerGroupsUi
import com.paligot.confily.style.theme.ConfilyTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
private fun PartnersGridContentPreview() {
    ConfilyTheme {
        PartnersGridContent(
            partners = PartnerGroupsUi.fake,
            onPartnerClick = {}
        )
    }
}
