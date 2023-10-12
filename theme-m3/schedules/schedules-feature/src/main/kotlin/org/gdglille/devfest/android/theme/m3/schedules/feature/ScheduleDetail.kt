package org.gdglille.devfest.android.theme.m3.schedules.feature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.openfeedback.android.OpenFeedbackConfig
import kotlinx.collections.immutable.persistentListOf
import org.gdglille.devfest.android.theme.m3.navigation.TopActions
import org.gdglille.devfest.android.theme.m3.style.appbars.TopAppBar
import org.gdglille.devfest.android.theme.m3.schedules.ui.speakers.SpeakerSection
import org.gdglille.devfest.android.theme.m3.schedules.ui.schedule.OpenFeedbackSection
import org.gdglille.devfest.android.theme.m3.schedules.ui.schedule.TalkAbstract
import org.gdglille.devfest.android.theme.m3.schedules.ui.schedule.TalkSection
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.android.theme.m3.style.actions.TopActionsUi
import org.gdglille.devfest.models.ui.TalkUi

@ExperimentalMaterial3Api
@Composable
fun ScheduleDetail(
    talk: TalkUi,
    openFeedbackState: OpenFeedbackConfig,
    onBackClicked: () -> Unit,
    onSpeakerClicked: (id: String) -> Unit,
    onShareClicked: (text: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val textShared = stringResource(id = R.string.input_share_talk, talk.title, talk.speakersSharing)
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.screen_schedule_detail),
                navigationIcon = { Back(onClick = onBackClicked) },
                topActionsUi = TopActionsUi(actions = persistentListOf(TopActions.share)),
                onActionClicked = {
                    when (it.id) {
                        TopActions.share.id -> {
                            onShareClicked(textShared)
                        }

                        else -> TODO()
                    }
                }
            )
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentPadding = it,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    TalkSection(talk = talk)
                }
                item {
                    TalkAbstract(abstract = talk.abstract)
                }
                if (talk.openFeedbackProjectId != null && talk.openFeedbackSessionId != null) {
                    item {
                        if (!LocalInspectionMode.current) {
                            OpenFeedbackSection(
                                openFeedbackProjectId = talk.openFeedbackProjectId!!,
                                openFeedbackSessionId = talk.openFeedbackSessionId!!,
                                openFeedbackState = openFeedbackState,
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
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    )
}

@ExperimentalMaterial3Api
@Preview
@Composable
private fun ScheduleDetailPreview() {
    Conferences4HallTheme {
        ScheduleDetail(
            talk = TalkUi.fake,
            openFeedbackState = OpenFeedbackConfig(
                context = LocalContext.current,
                firebaseConfig = OpenFeedbackConfig.FirebaseConfig(
                    projectId = "",
                    applicationId = "",
                    apiKey = "",
                    databaseUrl = ""
                ),
                openFeedbackProjectId = ""
            ),
            onBackClicked = {},
            onSpeakerClicked = {},
            onShareClicked = {}
        )
    }
}
