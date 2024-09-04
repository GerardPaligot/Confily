package org.gdglille.devfest.android.theme.m3.infos.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.models.ui.CoCUi
import com.paligot.confily.style.theme.Conferences4HallTheme
import org.gdglille.devfest.theme.m3.infos.screens.CoCScreen

@Preview
@Composable
private fun CoCPreview() {
    Conferences4HallTheme {
        CoCScreen(
            coc = CoCUi.fake,
            onReportByPhoneClicked = {},
            onReportByEmailClicked = {}
        )
    }
}
