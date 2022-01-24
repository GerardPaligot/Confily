package com.paligot.conferences.ui.components.speakers

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberImagePainter
import com.paligot.conferences.ui.theme.Conferences4HallTheme

@Composable
fun SpeakerAvatar(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    Image(
        painter = rememberImagePainter(url, builder = {
            val r = MaterialTheme.colors.primary.red
            val g = MaterialTheme.colors.primary.green
            val b = MaterialTheme.colors.primary.blue
            val color = Color.rgb((r * 100).toInt(), (g * 100).toInt(), (b * 100).toInt())
            this.placeholder(ColorDrawable(color))
        }),
        contentDescription = contentDescription,
        modifier = modifier.clip(CircleShape),
        contentScale = contentScale
    )
}

@Preview
@Composable
fun SpeakerAvatarPreview() {
    Conferences4HallTheme {
        SpeakerAvatar(speaker.url, contentDescription = null)
    }
}
