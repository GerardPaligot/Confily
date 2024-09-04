package org.gdglille.devfest.theme.m3.schedules.ui.schedule

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.models.ui.TalkUi
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme

@Preview
@Composable
fun TalkSectionPreview() {
    Conferences4HallTheme {
        TalkSection(talk = TalkUi.fake)
    }
}
