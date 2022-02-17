package com.paligot.conferences.ui.components.speakers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paligot.conferences.models.SpeakerUi
import com.paligot.conferences.ui.components.appbars.AppBarIcons
import com.paligot.conferences.ui.theme.Conferences4HallTheme

@Composable
fun SpeakerHeader(
    url: String,
    name: String,
    company: String,
    modifier: Modifier = Modifier,
    titleTextStyle: TextStyle = MaterialTheme.typography.h6,
    subtitleTextStyle: TextStyle = MaterialTheme.typography.subtitle2,
    color: Color = MaterialTheme.colors.onBackground,
    onBackClicked: () -> Unit
) {
    Box(modifier = modifier.fillMaxWidth()) {
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
        AppBarIcons.Back(color = color, onClick = onBackClicked)
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
