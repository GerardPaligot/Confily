package com.paligot.confily.style.speakers.avatar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.paligot.confily.style.theme.ConfilyTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MediumSpeakerAvatar(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    placeholder: Painter = SpeakerAvatarDefaults.placeholder,
    contentScale: ContentScale = ContentScale.Crop,
    shape: Shape = SpeakerAvatarDefaults.mediumShape
) {
    SpeakerAvatar(
        url = url,
        placeholder = placeholder,
        contentDescription = contentDescription,
        modifier = modifier.size(
            width = SpeakerAvatarMediumTokens.ContainerWidth,
            height = SpeakerAvatarMediumTokens.ContainerHeight
        ),
        contentScale = contentScale,
        shape = shape
    )
}

@Composable
fun LargeSpeakerAvatar(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    placeholder: Painter = SpeakerAvatarDefaults.placeholder,
    contentScale: ContentScale = ContentScale.Crop,
    shape: Shape = SpeakerAvatarDefaults.largeShape
) {
    SpeakerAvatar(
        url = url,
        placeholder = placeholder,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        shape = shape
    )
}

@Composable
internal fun SpeakerAvatar(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    placeholder: Painter = SpeakerAvatarDefaults.placeholder,
    border: BorderStroke? = null,
    shape: Shape = SpeakerAvatarDefaults.mediumShape
) {
    Image(
        painter = rememberAsyncImagePainter(
            model = url,
            placeholder = placeholder
        ),
        contentDescription = contentDescription,
        modifier = modifier
            .then(if (border != null) Modifier.border(border, shape) else Modifier)
            .clip(shape),
        contentScale = contentScale
    )
}

@Preview
@Composable
private fun MediumSpeakerAvatarPreview() {
    ConfilyTheme {
        Column {
            MediumSpeakerAvatar(
                url = "",
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
private fun LargeSpeakerAvatarPreview() {
    ConfilyTheme {
        Column {
            LargeSpeakerAvatar(
                url = "",
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .aspectRatio(1f)
            )
        }
    }
}
