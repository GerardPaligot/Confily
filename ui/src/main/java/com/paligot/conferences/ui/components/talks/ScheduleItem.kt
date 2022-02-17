package com.paligot.conferences.ui.components.talks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paligot.conferences.models.TalkItemUi
import com.paligot.conferences.ui.components.utils.Container
import com.paligot.conferences.ui.theme.Conferences4HallTheme
import com.paligot.conferences.ui.theme.placeholder

@Composable
fun ScheduleItem(
    time: String,
    talks: List<TalkItemUi>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onTalkClicked: (id: String) -> Unit,
    onFavoriteClicked: (id: String, isFavorite: Boolean) -> Unit
) {
    val timeSpace = 55.dp
    Box(modifier = modifier) {
        Box(
            modifier = Modifier.width(timeSpace),
            contentAlignment = Alignment.Center
        ) {
            Time(time = time, modifier = Modifier.placeholder(visible = isLoading))
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            talks.forEach {
                Container(onClick = { onTalkClicked(it.id) }, enabled = !isLoading) {
                    TalkItem(
                        talk = it,
                        modifier = Modifier.padding(start = timeSpace).placeholder(visible = isLoading),
                        onFavoriteClicked = onFavoriteClicked
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ScheduleItemPreview() {
    Conferences4HallTheme {
        ScheduleItem(
            time = "10:00",
            talks = arrayListOf(TalkItemUi.fake, TalkItemUi.fake),
            onTalkClicked = {},
            onFavoriteClicked = { _, _ -> }
        )
    }
}