package com.paligot.confily.schedules.panes

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import com.paligot.confily.schedules.panes.models.SessionUi
import com.paligot.confily.schedules.ui.schedule.OpenFeedbackSection
import com.paligot.confily.schedules.ui.schedule.SessionAbstract
import com.paligot.confily.schedules.ui.schedule.SessionInfoSection
import com.paligot.confily.schedules.ui.speakers.SpeakerSection
import com.paligot.confily.style.theme.SpacingTokens
import com.paligot.confily.style.theme.toDp

@ExperimentalMaterial3Api
@Composable
fun ScheduleDetailScreen(
    uiModel: SessionUi,
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
            SessionInfoSection(
                info = uiModel.info,
                speakers = uiModel.speakers
            )
        }
        item {
            SessionAbstract(abstract = uiModel.abstract)
        }
        if (uiModel.openFeedbackProjectId != null && uiModel.openFeedbackSessionId != null) {
            item {
                if (!LocalInspectionMode.current) {
                    OpenFeedbackSection(
                        openFeedbackProjectId = uiModel.openFeedbackProjectId!!,
                        openFeedbackSessionId = uiModel.openFeedbackSessionId!!,
                        canGiveFeedback = uiModel.canGiveFeedback
                    )
                }
            }
        }
        item {
            SpeakerSection(
                speakers = uiModel.speakers,
                onSpeakerItemClick = onSpeakerClicked
            )
        }
        item {
            Spacer(modifier = Modifier.height(SpacingTokens.ExtraLargeSpacing.toDp()))
        }
    }
}
