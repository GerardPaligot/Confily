package org.gdglille.devfest.android.ui.m2.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.openfeedback.android.OpenFeedbackConfig
import io.openfeedback.android.m2.OpenFeedback
import org.gdglille.devfest.android.ui.m2.R
import org.gdglille.devfest.android.ui.m2.TopActions
import org.gdglille.devfest.android.ui.m2.components.appbars.TopAppBar
import org.gdglille.devfest.android.ui.m2.components.speakers.SpeakerBox
import org.gdglille.devfest.android.ui.m2.components.speakers.SpeakerItem
import org.gdglille.devfest.android.ui.m2.components.talks.OpenFeedbackNotStarted
import org.gdglille.devfest.android.ui.m2.components.talks.TalkSection
import org.gdglille.devfest.android.ui.m2.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.models.TopActionsUi
import org.gdglille.devfest.models.TalkUi
import java.util.Locale

@Composable
fun ScheduleDetail(
    talk: TalkUi,
    openFeedbackState: OpenFeedbackConfig,
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
    onSpeakerClicked: (id: String) -> Unit,
    onShareClicked: (text: String) -> Unit
) {
    val textShared = stringResource(id = R.string.input_share_talk, talk.title, talk.speakersSharing)
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.screen_schedule_detail),
                navigationIcon = { Back(onClick = onBackClicked) },
                actions = TopActionsUi(topActions = listOf(TopActions.share)),
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
            val contentPadding = 8.dp
            LazyColumn(modifier = Modifier.fillMaxWidth(), contentPadding = it) {
                item {
                    TalkSection(
                        talk = talk,
                        modifier = Modifier.padding(horizontal = contentPadding)
                    )
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        talk.speakers.forEach {
                            SpeakerBox(onClick = { onSpeakerClicked(it.id) }) {
                                SpeakerItem(
                                    speakerUi = it,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = contentPadding, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    if (!talk.canGiveFeedback) {
                        OpenFeedbackNotStarted(
                            modifier = Modifier.padding(horizontal = contentPadding)
                        )
                    } else if (talk.openFeedbackSessionId != null) {
                        OpenFeedback(
                            openFeedbackState = openFeedbackState,
                            sessionId = talk.openFeedbackSessionId!!,
                            language = Locale.getDefault().language,
                            modifier = Modifier.padding(horizontal = contentPadding)
                        )
                    }
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
