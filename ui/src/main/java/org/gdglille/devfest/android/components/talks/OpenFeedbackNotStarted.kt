package org.gdglille.devfest.android.components.talks

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.R

@Composable
fun OpenFeedbackNotStarted(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onBackground,
    style: TextStyle = MaterialTheme.typography.body2
) {
    Text(
        text = stringResource(R.string.text_openfeedback_not_started),
        color = color,
        style = style,
        modifier = modifier
            .border(width = 1.dp, color = color)
            .padding(12.dp)
    )
}

@Preview
@Composable
fun OpenFeedbackNotStartedPreview() {
    Conferences4HallTheme {
        OpenFeedbackNotStarted()
    }
}
