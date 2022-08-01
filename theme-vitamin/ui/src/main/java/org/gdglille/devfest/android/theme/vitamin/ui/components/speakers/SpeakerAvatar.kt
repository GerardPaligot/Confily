package org.gdglille.devfest.android.theme.vitamin.ui.components.speakers

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.imageLoader
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.models.SpeakerUi

@Composable
fun SpeakerAvatar(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.FillWidth,
    shape: Shape = RectangleShape
) {
    Image(
        painter = rememberAsyncImagePainter(
            model = url,
            placeholder = ColorPainter(VitaminTheme.colors.vtmnBackgroundBrandPrimary),
            imageLoader = LocalContext.current.imageLoader
        ),
        contentDescription = contentDescription,
        contentScale = contentScale,
        modifier = modifier.clip(shape)
    )
}

@Composable
fun SpeakerAvatarBordered(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    borderColor: Color = VitaminTheme.colors.vtmnBorderPrimary,
    contentScale: ContentScale = ContentScale.Crop,
    shape: Shape = CircleShape
) {
    Image(
        painter = rememberAsyncImagePainter(
            model = url,
            placeholder = ColorPainter(VitaminTheme.colors.vtmnBackgroundBrandPrimary),
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
        SpeakerAvatar(
            url = SpeakerUi.fake.url,
            contentDescription = null,
            modifier = Modifier.size(50.dp)
        )
    }
}

@Preview
@Composable
fun SpeakerAvatarBorderedPreview() {
    Conferences4HallTheme {
        SpeakerAvatarBordered(
            url = SpeakerUi.fake.url,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }
}
