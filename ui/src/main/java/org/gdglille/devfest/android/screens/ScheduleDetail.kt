package org.gdglille.devfest.android.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.ui.R
import org.gdglille.devfest.android.components.appbars.ActionItem
import org.gdglille.devfest.android.components.appbars.ActionItemId
import org.gdglille.devfest.android.components.appbars.TopAppBar
import org.gdglille.devfest.android.components.speakers.SpeakerBox
import org.gdglille.devfest.android.components.speakers.SpeakerItem
import org.gdglille.devfest.android.components.talks.TalkSection
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import io.openfeedback.android.OpenFeedbackConfig
import io.openfeedback.android.components.OpenFeedback
import io.openfeedback.android.components.rememberOpenFeedbackState
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
    val speakers = talk.speakers.joinToString(", ") { speaker ->
        if (speaker.twitter == null) speaker.name
        else "${speaker.name} (@${speaker.twitter})"
    }
    val textShared = stringResource(id = R.string.input_share_talk, talk.title, speakers)
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.screen_schedule_detail),
                navigationIcon = { Back(onClick = onBackClicked) },
                actions = arrayListOf(
                    ActionItem(
                        id = ActionItemId.ShareActionItem,
                        icon = Icons.Default.Share,
                        contentDescription = R.string.action_share_talk,
                        formatArgs = arrayListOf(talk.title)
                    )
                ),
                onActionClicked = {
                    when (it) {
                        ActionItemId.ShareActionItem -> { onShareClicked(textShared) }
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
                talk.openFeedbackSessionId?.let {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        OpenFeedback(
                            openFeedbackState = openFeedbackState,
                            sessionId = it,
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
            openFeedbackState = rememberOpenFeedbackState(
                projectId = "",
                firebaseConfig = OpenFeedbackConfig.FirebaseConfig(
                    projectId = "",
                    applicationId = "",
                    apiKey = "",
                    databaseUrl = ""
                )
            ),
            onBackClicked = {},
            onSpeakerClicked = {},
            onShareClicked = {}
        )
    }
}
