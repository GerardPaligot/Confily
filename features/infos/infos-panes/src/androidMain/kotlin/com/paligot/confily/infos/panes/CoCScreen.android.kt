package com.paligot.confily.infos.panes

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.infos.ui.models.CoCUi
import com.paligot.confily.style.theme.ConfilyTheme

@Preview
@Composable
private fun CoCPreview() {
    ConfilyTheme {
        CoCScreen(
            coc = CoCUi.fake,
            onReportByPhoneClicked = {},
            onReportByEmailClicked = {}
        )
    }
}
