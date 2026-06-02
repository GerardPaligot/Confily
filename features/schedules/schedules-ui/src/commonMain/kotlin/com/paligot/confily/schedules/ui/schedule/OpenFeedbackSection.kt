package com.paligot.confily.schedules.ui.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.text_openfeedback_not_started
import com.paligot.confily.resources.text_openfeedback_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun OpenFeedbackSection(
    openFeedbackEnabled: Boolean,
    openFeedbackProjectId: String,
    openFeedbackSessionId: String,
    openFeedbackUrl: String,
    canGiveFeedback: Boolean,
    launchUrl: (String) -> Unit,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.titleLarge
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        Text(
            text = stringResource(Resource.string.text_openfeedback_title),
            style = style
        )
        if (openFeedbackEnabled) {
            Feedback(
                projectId = openFeedbackProjectId,
                sessionId = openFeedbackSessionId,
                canGiveFeedback = canGiveFeedback
            )
        } else if (canGiveFeedback) {
            GiveFeedbackButton(onClick = { launchUrl(openFeedbackUrl) })
        } else {
            Text(
                text = stringResource(Resource.string.text_openfeedback_not_started),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
