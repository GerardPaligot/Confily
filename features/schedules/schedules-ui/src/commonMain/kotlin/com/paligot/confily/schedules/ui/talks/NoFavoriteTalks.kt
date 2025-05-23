package com.paligot.confily.schedules.ui.talks

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
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.text_agenda_no_favorites
import com.paligot.confily.style.theme.ConfilyTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

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

@Preview
@Composable
private fun NoFavoriteTalksPreview() {
    ConfilyTheme {
        NoFavoriteTalks()
    }
}
