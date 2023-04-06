package org.gdglille.devfest.android.theme.vitamin.ui.screens.agenda

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decathlon.vitamin.compose.appbars.topbars.ActionItem
import io.openfeedback.android.OpenFeedbackConfig
import kotlinx.collections.immutable.persistentListOf
import org.gdglille.devfest.android.theme.vitamin.ui.components.appbars.TopAppBar
import org.gdglille.devfest.android.theme.vitamin.ui.components.speakers.SpeakerSection
import org.gdglille.devfest.android.theme.vitamin.ui.components.talks.OpenFeedbackSection
import org.gdglille.devfest.android.theme.vitamin.ui.components.talks.TalkSection
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.models.TalkUi
import com.decathlon.vitamin.compose.foundation.R as RVitamin

@Composable
fun ScheduleDetail(
    talk: TalkUi,
    openFeedbackState: OpenFeedbackConfig,
    onBackClicked: () -> Unit,
    onSpeakerClicked: (id: String) -> Unit,
    onShareClicked: (text: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val textShared = stringResource(id = R.string.input_share_talk, talk.title, talk.speakersSharing)
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.screen_schedule_detail),
                navigationIcon = {
                    Context(
                        onClick = onBackClicked,
                        contentDescription = stringResource(id = R.string.action_back)
                    )
                },
                actions = persistentListOf(
                    ActionItem(
                        icon = painterResource(id = RVitamin.drawable.ic_vtmn_share_line),
                        contentDescription = stringResource(
                            id = R.string.action_share_talk, talk.title
                        ),
                        onClick = { onShareClicked(textShared) }
                    )
                )
            )
        },
        content = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                contentPadding = it,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    TalkSection(talk = talk)
                }
                if (talk.openFeedbackProjectId != null && talk.openFeedbackSessionId != null) {
                    item {
                        if (!LocalInspectionMode.current) {
                            OpenFeedbackSection(
                                openFeedbackState = openFeedbackState,
                                openFeedbackProjectId = talk.openFeedbackProjectId!!,
                                openFeedbackSessionId = talk.openFeedbackSessionId!!,
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

@Preview
@Composable
fun TalkDetailPreview() {
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
