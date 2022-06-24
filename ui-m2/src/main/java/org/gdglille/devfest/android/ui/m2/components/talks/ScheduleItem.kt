package org.gdglille.devfest.android.ui.m2.components.talks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.ui.m2.components.utils.Container
import org.gdglille.devfest.android.ui.m2.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.m2.theme.LocalAccessibility
import org.gdglille.devfest.android.ui.m2.theme.placeholder
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
    val hasTalkbackActivated =
        LocalAccessibility.current.isEnabled && LocalAccessibility.current.isTouchExplorationEnabled
    val timeSpace = 55.dp
    ScheduleItemContainer(hasTalkbackActivated = hasTalkbackActivated, modifier = modifier) {
        Box(
            modifier = Modifier.width(timeSpace),
            contentAlignment = Alignment.Center
        ) {
            Time(time = time, modifier = Modifier.placeholder(visible = isLoading))
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            talks.forEach {
                Container(onClick = { onTalkClicked(it.id) }, enabled = !isLoading && !it.isPause) {
                    val talkModifier =
                        if (hasTalkbackActivated) Modifier else Modifier.padding(start = timeSpace)
                    TalkItem(
                        talk = it,
                        modifier = talkModifier.placeholder(visible = isLoading),
                        onFavoriteClicked = onFavoriteClicked
                    )
                }
            }
        }
    }
}

@Composable
internal fun ScheduleItemContainer(
    hasTalkbackActivated: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    if (hasTalkbackActivated) {
        Row(modifier = modifier) {
            content()
        }
    } else {
        Box(modifier = modifier) {
            content()
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
            onFavoriteClicked = { }
        )
    }
}
