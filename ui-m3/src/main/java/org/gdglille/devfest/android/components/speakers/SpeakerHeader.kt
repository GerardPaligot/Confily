package org.gdglille.devfest.android.components.speakers

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.components.appbars.AppBarIcons
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.android.theme.LocalAccessibility
import org.gdglille.devfest.models.SpeakerUi

@Composable
fun SpeakerHeader(
    url: String,
    name: String,
    company: String,
    modifier: Modifier = Modifier,
    titleTextStyle: TextStyle = MaterialTheme.typography.titleLarge,
    subtitleTextStyle: TextStyle = MaterialTheme.typography.titleSmall,
    color: Color = MaterialTheme.colorScheme.onBackground,
    onBackClicked: () -> Unit
) {
    val hasTalkbackActivated = LocalAccessibility.current.isEnabled && LocalAccessibility.current.isTouchExplorationEnabled
    SpeakerHeaderContainer(hasTalkbackActivated = hasTalkbackActivated, modifier = modifier.fillMaxWidth()) {
        AppBarIcons.Back(color = color, onClick = onBackClicked)
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))
            SpeakerAvatar(
                url = url,
                contentDescription = null,
                modifier = Modifier.size(96.dp)
            )
            Spacer(Modifier.height(24.dp))
            Text(text = name, style = titleTextStyle, color = color)
            Text(text = company, style = subtitleTextStyle, color = color)
        }
    }
}

@Composable
internal fun SpeakerHeaderContainer(
    hasTalkbackActivated: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    if (hasTalkbackActivated) {
        Column(modifier = modifier) {
            content()
        }
    } else {
        Box(modifier = modifier) {
            content()
        }
    }
}

@Preview
@Composable
fun SpeakerHeaderPreview() {
    Conferences4HallTheme {
        SpeakerHeader(
            url = SpeakerUi.fake.url,
            name = SpeakerUi.fake.name,
            company = SpeakerUi.fake.company,
            onBackClicked = {}
        )
    }
}
