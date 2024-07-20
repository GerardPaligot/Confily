package org.gdglille.devfest.theme.m3.schedules.ui.talks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.shared.resources.Resource
import org.gdglille.devfest.android.shared.resources.text_agenda_no_favorites
import org.jetbrains.compose.resources.stringResource

@Composable
fun NoFavoriteTalks(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            text = stringResource(Resource.string.text_agenda_no_favorites),
            textAlign = TextAlign.Center,
            color = color,
            modifier = Modifier.padding(horizontal = 50.dp).align(Alignment.Center)
        )
    }
}
