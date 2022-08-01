package org.gdglille.devfest.android.theme.vitamin.ui.components.speakers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import compose.icons.fontawesomeicons.brands.Twitter
import org.gdglille.devfest.android.theme.vitamin.ui.R
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.models.EventUi
import org.gdglille.devfest.models.SpeakerUi

object Socials {
    @Composable
    fun Twitter(
        text: String,
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        SocialItem(
            painter = painterResource(R.drawable.ic_vtmn_twitter_line),
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
        SocialItem(
            painter = painterResource(R.drawable.ic_vtmn_google_line),
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
        SocialItem(
            painter = painterResource(R.drawable.ic_vtmn_google_line),
            contentDescription = stringResource(id = R.string.semantic_linkedin, text),
            modifier = modifier,
            onClick = onClick
        )
    }
}

@Composable
internal fun SocialItem(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        shape = RoundedCornerShape(4.dp),
        backgroundColor = VitaminTheme.colors.vtmnBackgroundBrandSecondary,
        contentColor = VitaminTheme.colors.vtmnContentAction,
        modifier = modifier,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            hoveredElevation = 0.dp,
            focusedElevation = 0.dp
        )
    ) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview
@Composable
fun SocialItemPreview() {
    Conferences4HallTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Socials.Twitter(
                text = SpeakerUi.fake.twitter!!,
                onClick = {}
            )
            Socials.GitHub(
                text = SpeakerUi.fake.github!!,
                onClick = {}
            )
            Socials.LinkedIn(
                text = EventUi.fake.eventInfo.linkedin!!,
                onClick = {}
            )
        }
    }
}
