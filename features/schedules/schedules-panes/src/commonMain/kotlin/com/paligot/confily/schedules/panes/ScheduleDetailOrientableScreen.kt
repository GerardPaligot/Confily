package com.paligot.confily.schedules.panes

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
import com.paligot.confily.models.ui.TalkUi
import com.paligot.confily.navigation.TopActions
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.input_share_talk
import com.paligot.confily.resources.screen_schedule_detail
import com.paligot.confily.style.theme.actions.TopActionsUi
import com.paligot.confily.style.theme.appbars.TopAppBar
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource

@ExperimentalMaterial3Api
@Composable
fun ScheduleDetailOrientableScreen(
    talk: TalkUi,
    onBackClicked: () -> Unit,
    onSpeakerClicked: (id: String) -> Unit,
    onShareClicked: (text: String) -> Unit,
    modifier: Modifier = Modifier,
    isLandscape: Boolean = false
) {
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
                topActionsUi = TopActionsUi(
                    actions = persistentListOf(
                        TopActions.share
                    )
                ),
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
            if (isLandscape) {
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
