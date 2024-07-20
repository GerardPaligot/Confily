package org.gdglille.devfest.theme.m3.schedules.ui.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.shared.resources.Resource
import org.gdglille.devfest.android.shared.resources.text_openfeedback_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun OpenFeedbackSection(
    openFeedbackProjectId: String,
    openFeedbackSessionId: String,
    canGiveFeedback: Boolean,
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
        Feedback(
            projectId = openFeedbackProjectId,
            sessionId = openFeedbackSessionId,
            canGiveFeedback = canGiveFeedback
        )
    }
}
