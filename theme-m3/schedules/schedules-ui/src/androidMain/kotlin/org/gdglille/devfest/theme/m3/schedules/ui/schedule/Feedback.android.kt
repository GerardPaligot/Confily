package org.gdglille.devfest.theme.m3.schedules.ui.schedule

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.openfeedback.OpenFeedback

@Composable
actual fun Feedback(
    projectId: String,
    sessionId: String,
    canGiveFeedback: Boolean,
    modifier: Modifier
) {
    OpenFeedback(
        projectId = projectId,
        sessionId = sessionId,
        isReady = canGiveFeedback,
        modifier = modifier
    )
}
