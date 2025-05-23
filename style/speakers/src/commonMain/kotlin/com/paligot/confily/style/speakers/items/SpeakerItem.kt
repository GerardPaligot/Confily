package com.paligot.confily.style.speakers.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.paligot.confily.style.speakers.avatar.LargeSpeakerAvatar
import com.paligot.confily.style.theme.ConfilyTheme
import com.paligot.confily.style.theme.toDp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LargeSpeakerItem(
    name: String,
    description: String,
    url: String,
    modifier: Modifier = Modifier,
    color: Color = SpeakerItemDefaults.containerColor,
    nameTextStyle: TextStyle = SpeakerItemDefaults.nameTextStyle,
    descriptionTextStyle: TextStyle = SpeakerItemDefaults.descriptionTextStyle,
    shape: Shape = SpeakerItemDefaults.containerShape,
    onClick: () -> Unit
) {
    Card(modifier = modifier, shape = shape, onClick = onClick) {
        Column {
            LargeSpeakerAvatar(
                url = url,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SpeakerItemLargeTokens.TextPadding.toDp()),
                verticalArrangement = Arrangement.spacedBy(SpeakerItemLargeTokens.TextBetweenSpacing.toDp())
            ) {
                Text(
                    text = name,
                    color = color,
                    style = nameTextStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = description,
                    color = color,
                    style = descriptionTextStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
private fun SpeakerItemPreview() {
    ConfilyTheme {
        LargeSpeakerItem(
            name = "Gérard Paligot",
            description = "Senior Staff Engineer @Decathlon DIgital",
            url = "",
            onClick = {}
        )
    }
}
