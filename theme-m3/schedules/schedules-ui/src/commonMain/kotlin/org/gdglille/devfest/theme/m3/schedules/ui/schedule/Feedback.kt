package org.gdglille.devfest.theme.m3.schedules.ui.schedule

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun Feedback(
    projectId: String,
    sessionId: String,
    canGiveFeedback: Boolean,
    modifier: Modifier = Modifier
)
