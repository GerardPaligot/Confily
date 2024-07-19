package org.gdglille.devfest.theme.m3.style.speakers.avatars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import kotlinx.collections.immutable.ImmutableList
import org.gdglille.devfest.android.theme.m3.style.toDp

@Composable
fun SmallLabeledSpeakersAvatar(
    label: String,
    urls: ImmutableList<String>,
    modifier: Modifier = Modifier,
    color: Color = LabeledSpeakersAvatarDefaults.contentColor,
    style: TextStyle = LabeledSpeakersAvatarDefaults.smallTextStyle
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(LabeledSpeakersAvatarSmallTokens.SpacingBetween.toDp()),
        modifier = modifier
    ) {
        SmallBorderedSpeakersAvatar(urls = urls, descriptions = null)
        Text(text = label, style = style, color = color)
    }
}
