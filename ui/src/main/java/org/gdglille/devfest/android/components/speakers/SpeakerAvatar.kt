package org.gdglille.devfest.android.components.speakers

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.models.SpeakerUi

@Composable
fun SpeakerAvatar(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    Image(
        painter = rememberAsyncImagePainter(
            model = url,
            placeholder = ColorPainter(MaterialTheme.colors.primary)
        ),
        contentDescription = contentDescription,
        modifier = modifier.clip(CircleShape),
        contentScale = contentScale
    )
}

@Preview
@Composable
fun SpeakerAvatarPreview() {
    Conferences4HallTheme {
        SpeakerAvatar(SpeakerUi.fake.url, contentDescription = null)
    }
}
