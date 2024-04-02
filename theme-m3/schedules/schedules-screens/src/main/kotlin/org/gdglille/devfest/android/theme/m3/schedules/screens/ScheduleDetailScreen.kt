package org.gdglille.devfest.android.theme.m3.schedules.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import io.openfeedback.viewmodels.OpenFeedbackFirebaseConfig
import org.gdglille.devfest.android.theme.m3.schedules.ui.schedule.OpenFeedbackSection
import org.gdglille.devfest.android.theme.m3.schedules.ui.schedule.TalkAbstract
import org.gdglille.devfest.android.theme.m3.schedules.ui.schedule.TalkSection
import org.gdglille.devfest.android.theme.m3.schedules.ui.speakers.SpeakerSection
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.SpacingTokens
import org.gdglille.devfest.android.theme.m3.style.toDp
import org.gdglille.devfest.models.ui.TalkUi

@ExperimentalMaterial3Api
@Composable
fun ScheduleDetailScreen(
    talk: TalkUi,
    openFeedbackFirebaseConfig: OpenFeedbackFirebaseConfig?,
    onSpeakerClicked: (id: String) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(SpacingTokens.None.toDp())
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = SpacingTokens.LargeSpacing.toDp()),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.LargeSpacing.toDp()),
        state = state
    ) {
        item {
            Spacer(modifier = Modifier.height(SpacingTokens.LargeSpacing.toDp()))
            TalkSection(talk = talk)
        }
        item {
            TalkAbstract(abstract = talk.abstract)
        }
        if (
            talk.openFeedbackProjectId != null &&
            talk.openFeedbackSessionId != null &&
            openFeedbackFirebaseConfig != null
        ) {
            item {
                if (!LocalInspectionMode.current) {
                    OpenFeedbackSection(
                        openFeedbackProjectId = talk.openFeedbackProjectId!!,
                        openFeedbackSessionId = talk.openFeedbackSessionId!!,
                        openFeedbackFirebaseConfig = openFeedbackFirebaseConfig,
                        canGiveFeedback = talk.canGiveFeedback
                    )
                }
            }
        }
        item {
            SpeakerSection(
                speakers = talk.speakers,
                onSpeakerItemClick = onSpeakerClicked
            )
        }
        item {
            Spacer(modifier = Modifier.height(SpacingTokens.ExtraLargeSpacing.toDp()))
        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
private fun ScheduleDetailPreview() {
    Conferences4HallTheme {
        Surface {
            ScheduleDetailScreen(
                talk = TalkUi.fake,
                openFeedbackFirebaseConfig = OpenFeedbackFirebaseConfig(
                    context = LocalContext.current,
                    projectId = "",
                    applicationId = "",
                    apiKey = "",
                    databaseUrl = ""
                ),
                onSpeakerClicked = {}
            )
        }
    }
}
