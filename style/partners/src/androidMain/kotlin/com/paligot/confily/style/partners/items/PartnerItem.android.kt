package com.paligot.confily.style.partners.items

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.style.theme.ConfilyTheme

@Preview
@Composable
private fun PartnerItemPreview() {
    ConfilyTheme {
        PartnerItem(
            url = "",
            contentDescription = "Gerard Inc.",
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
    }
}
