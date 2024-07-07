package org.gdglille.devfest.android.theme.m3.schedules.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import kotlinx.collections.immutable.persistentListOf
import org.gdglille.devfest.android.shared.resources.Resource
import org.gdglille.devfest.android.shared.resources.input_share_talk
import org.gdglille.devfest.android.shared.resources.screen_schedule_detail
import org.gdglille.devfest.android.theme.m3.navigation.TopActions
import org.gdglille.devfest.android.theme.m3.style.actions.TopActionsUi
import org.gdglille.devfest.android.theme.m3.style.appbars.TopAppBar
import org.gdglille.devfest.models.ui.TalkUi
import org.jetbrains.compose.resources.stringResource

@ExperimentalMaterial3Api
@Composable
fun ScheduleDetailOrientableScreen(
    talk: TalkUi,
    onBackClicked: () -> Unit,
    onSpeakerClicked: (id: String) -> Unit,
    onShareClicked: (text: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val orientation = LocalConfiguration.current
    val textShared =
        stringResource(Resource.string.input_share_talk, talk.title, talk.speakersSharing)
    val state = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = stringResource(Resource.string.screen_schedule_detail),
                navigationIcon = { Back(onClick = onBackClicked) },
                topActionsUi = TopActionsUi(actions = persistentListOf(TopActions.share)),
                onActionClicked = {
                    when (it.id) {
                        TopActions.share.id -> {
                            onShareClicked(textShared)
                        }

                        else -> TODO()
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        content = {
            if (orientation.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.padding(it)
                ) {
                    ScheduleDetailScreen(
                        talk = talk,
                        onSpeakerClicked = onSpeakerClicked,
                        modifier = Modifier.weight(1f),
                        state = state
                    )
                    FeedbackScreen(
                        openFeedbackProjectId = talk.openFeedbackProjectId,
                        openFeedbackSessionId = talk.openFeedbackSessionId,
                        canGiveFeedback = talk.canGiveFeedback,
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                ScheduleDetailScreen(
                    talk = talk,
                    onSpeakerClicked = onSpeakerClicked,
                    state = state,
                    contentPadding = it
                )
            }
        }
    )
}
