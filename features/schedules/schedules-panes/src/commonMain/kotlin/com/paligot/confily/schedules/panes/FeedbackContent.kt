package com.paligot.confily.schedules.panes

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.text_feedback_not_configured
import com.paligot.confily.schedules.ui.schedule.OpenFeedbackSection
import com.paligot.confily.style.theme.SpacingTokens
import com.paligot.confily.style.theme.toDp
import org.jetbrains.compose.resources.stringResource

@Composable
fun FeedbackContent(
    openFeedbackProjectId: String?,
    openFeedbackSessionId: String?,
    canGiveFeedback: Boolean,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = SpacingTokens.LargeSpacing.toDp())
    ) {
        item {
            if (openFeedbackProjectId != null && openFeedbackSessionId != null) {
                OpenFeedbackSection(
                    openFeedbackProjectId = openFeedbackProjectId,
                    openFeedbackSessionId = openFeedbackSessionId,
                    canGiveFeedback = canGiveFeedback
                )
            } else {
                Text(
                    text = stringResource(Resource.string.text_feedback_not_configured),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .border(width = 1.dp, color = MaterialTheme.colorScheme.onBackground)
                        .padding(SpacingTokens.MediumSpacing.toDp())
                )
            }
        }
    }
}
