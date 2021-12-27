package com.paligot.conferences.android.components.speakers

import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIconType
import com.guru.fontawesomecomposelib.FaIcons
import com.paligot.conferences.android.theme.ConferenceTheme

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
      icon = FaIcons.Twitter,
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
      icon = FaIcons.Github,
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
  icon: FaIconType.BrandIcon,
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
      .clickable(
        interactionSource = interactionSource,
        indication = indication,
        role = Role.Button,
        onClick = onClick
      )
      .padding(8.dp)
  ) {
    FaIcon(
      faIcon = icon,
      tint = color
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
  ConferenceTheme {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      Socials.Twitter(
        text = speaker.twitter!!,
        onClick = {}
      )
      Socials.GitHub(
        text = speaker.github!!,
        onClick = {}
      )
    }
  }
}
