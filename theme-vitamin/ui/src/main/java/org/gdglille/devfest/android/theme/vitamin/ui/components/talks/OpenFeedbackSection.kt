package org.gdglille.devfest.android.theme.vitamin.ui.components.talks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import io.openfeedback.android.OpenFeedbackConfig
import io.openfeedback.android.components.OpenFeedback
import org.gdglille.devfest.android.theme.vitamin.ui.R
import java.util.Locale

@Composable
fun OpenFeedbackSection(
    openFeedbackSessionId: String?,
    canGiveFeedback: Boolean,
    openFeedbackState: OpenFeedbackConfig,
    modifier: Modifier = Modifier
) {
    if (!canGiveFeedback) {
        OpenFeedbackNotStarted(modifier = modifier)
    } else if (openFeedbackSessionId != null) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
        ) {
            Text(
                text = stringResource(R.string.text_openfeedback_title),
                style = VitaminTheme.typography.h6
            )
            OpenFeedback(
                openFeedbackState = openFeedbackState,
                sessionId = openFeedbackSessionId,
                language = Locale.getDefault().language
            )
        }
    }
}
