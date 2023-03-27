package org.gdglille.devfest.android.theme.vitamin.ui.components.speakers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.vitamin.ui.components.buttons.IconButtonTertiary
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.models.EventUi
import org.gdglille.devfest.models.SpeakerUi
import com.decathlon.vitamin.compose.foundation.R as RVitamin

object Socials {
    @Composable
    fun Twitter(
        text: String,
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        IconButtonTertiary(
            painter = painterResource(RVitamin.drawable.ic_vtmn_twitter_line),
            contentDescription = stringResource(id = R.string.semantic_twitter, text),
            modifier = modifier,
            onClick = onClick
        )
    }

    @Composable
    fun GitHub(
        text: String,
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        IconButtonTertiary(
            painter = painterResource(RVitamin.drawable.ic_vtmn_google_line),
            contentDescription = stringResource(id = R.string.semantic_github, text),
            modifier = modifier,
            onClick = onClick
        )
    }

    @Composable
    fun LinkedIn(
        text: String,
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        IconButtonTertiary(
            painter = painterResource(RVitamin.drawable.ic_vtmn_google_line),
            contentDescription = stringResource(id = R.string.semantic_linkedin, text),
            modifier = modifier,
            onClick = onClick
        )
    }
}

@Preview
@Composable
fun SocialItemPreview() {
    Conferences4HallTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Socials.Twitter(
                text = SpeakerUi.fake.name,
                onClick = {}
            )
            Socials.GitHub(
                text = SpeakerUi.fake.name,
                onClick = {}
            )
            Socials.LinkedIn(
                text = EventUi.fake.eventInfo.name,
                onClick = {}
            )
        }
    }
}
