package org.gdglille.devfest.android.ui.m2.components.speakers

import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.brands.Github
import compose.icons.fontawesomeicons.brands.LinkedinIn
import compose.icons.fontawesomeicons.brands.Twitter
import org.gdglille.devfest.android.ui.m2.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.m2.R
import org.gdglille.devfest.models.EventUi
import org.gdglille.devfest.models.SpeakerUi

object Socials {
    @Composable
    fun Twitter(
        text: String,
        modifier: Modifier = Modifier,
        style: TextStyle = MaterialTheme.typography.caption,
        color: Color = MaterialTheme.colors.onBackground,
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        indication: Indication = rememberRipple(),
        onClick: () -> Unit
    ) {
        SocialItem(
            text = text,
            imageVector = FontAwesomeIcons.Brands.Twitter,
            contentDescription = stringResource(id = R.string.semantic_twitter, text),
            modifier = modifier,
            style = style,
            color = color,
            interactionSource = interactionSource,
            indication = indication,
            onClick = onClick
        )
    }

    @Composable
    fun GitHub(
        text: String,
        modifier: Modifier = Modifier,
        style: TextStyle = MaterialTheme.typography.caption,
        color: Color = MaterialTheme.colors.onBackground,
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        indication: Indication = rememberRipple(),
        onClick: () -> Unit
    ) {
        SocialItem(
            text = text,
            imageVector = FontAwesomeIcons.Brands.Github,
            contentDescription = stringResource(id = R.string.semantic_github, text),
            modifier = modifier,
            style = style,
            color = color,
            interactionSource = interactionSource,
            indication = indication,
            onClick = onClick
        )
    }

    @Composable
    fun LinkedIn(
        text: String,
        modifier: Modifier = Modifier,
        style: TextStyle = MaterialTheme.typography.caption,
        color: Color = MaterialTheme.colors.onBackground,
        interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
        indication: Indication = rememberRipple(),
        onClick: () -> Unit
    ) {
        SocialItem(
            text = text,
            imageVector = FontAwesomeIcons.Brands.LinkedinIn,
            contentDescription = stringResource(id = R.string.semantic_linkedin, text),
            modifier = modifier,
            style = style,
            color = color,
            interactionSource = interactionSource,
            indication = indication,
            onClick = onClick
        )
    }
}

@Composable
internal fun SocialItem(
    text: String,
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.caption,
    color: Color = MaterialTheme.colors.onBackground,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication? = LocalIndication.current,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .semantics {
                contentDescription?.let {
                    this.contentDescription = contentDescription
                }
            }
            .clickable(
                interactionSource = interactionSource,
                indication = indication,
                role = Role.Button,
                onClick = onClick
            )
            .padding(8.dp)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = text,
            style = style,
            color = color
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
