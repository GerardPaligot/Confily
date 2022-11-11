package org.gdglille.devfest.android.components.speakers

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.models.SpeakerUi

@Composable
fun SpeakerAvatar(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    shape: Shape = CircleShape
) {
    Image(
        painter = rememberAsyncImagePainter(
            model = url,
            placeholder = ColorPainter(MaterialTheme.colorScheme.primary),
            imageLoader = LocalContext.current.imageLoader
        ),
        contentDescription = contentDescription,
        modifier = modifier.clip(shape),
        contentScale = contentScale
    )
}

@Composable
fun SpeakerAvatarBordered(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    borderColor: Color = MaterialTheme.colorScheme.onPrimary,
    contentScale: ContentScale = ContentScale.Crop,
    shape: Shape = CircleShape
) {
    Image(
        painter = rememberAsyncImagePainter(
            model = url,
            placeholder = ColorPainter(MaterialTheme.colorScheme.primary),
            imageLoader = LocalContext.current.imageLoader
        ),
        contentDescription = contentDescription,
        modifier = modifier
            .border(width = 1.dp, color = borderColor, shape)
            .clip(shape),
        contentScale = contentScale
    )
}

@Preview
@Composable
fun SpeakerAvatarPreview() {
    Conferences4HallTheme {
        Column {
            SpeakerAvatar(
                SpeakerUi.fake.url,
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            SpeakerAvatarBordered(
                SpeakerUi.fake.url,
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
        }
    }
}
