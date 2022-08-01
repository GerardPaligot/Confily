package org.gdglille.devfest.android.theme.vitamin.ui.components.talks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.android.theme.vitamin.ui.theme.placeholder
import org.gdglille.devfest.models.TalkItemUi

@Composable
fun ScheduleItem(
    time: String,
    talks: List<TalkItemUi>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onTalkClicked: (id: String) -> Unit,
    onFavoriteClicked: (TalkItemUi) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Time(time = time, isLoading = isLoading)
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            talks.forEach {
                if (it.isPause) {
                    PauseItem()
                } else {
                    TalkItem(
                        talk = it,
                        modifier = Modifier.placeholder(visible = isLoading),
                        onTalkItemClicked = { onTalkClicked(it.id) },
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
        Scaffold(backgroundColor = VitaminTheme.colors.vtmnBackgroundSecondary) {
            ScheduleItem(
                time = "10:00",
                talks = arrayListOf(TalkItemUi.fake, TalkItemUi.fake),
                onTalkClicked = {},
                onFavoriteClicked = { },
                modifier = Modifier.padding(it)
            )
        }
    }
}
