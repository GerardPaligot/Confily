package org.gdglille.devfest.android.theme.vitamin.ui.components.talks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.R

@Composable
fun NoFavoriteTalks(
    modifier: Modifier = Modifier,
    color: Color = VitaminTheme.colors.vtmnContentPrimary
) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.text_agenda_no_favorites),
            textAlign = TextAlign.Center,
            color = color,
            modifier = Modifier.padding(horizontal = 50.dp).align(Alignment.Center)
        )
    }
}

@Preview
@Composable
fun NoFavoriteTalksPreview() {
    Conferences4HallTheme {
        NoFavoriteTalks()
    }
}
