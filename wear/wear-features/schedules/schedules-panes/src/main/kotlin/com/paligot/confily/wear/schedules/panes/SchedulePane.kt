package com.paligot.confily.wear.schedules.panes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.ListSubheader
import androidx.wear.compose.material3.LocalContentColor
import androidx.wear.compose.material3.LocalTextStyle
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.ScrollIndicator
import androidx.wear.compose.material3.Text
import coil3.compose.rememberAsyncImagePainter
import com.paligot.confily.models.ui.TalkUi
import com.paligot.confily.wear.theme.buttons.ExtendedActionButton
import com.paligot.confily.wear.theme.markdown.MarkdownText

@Composable
fun SchedulePane(
    modelUi: TalkUi,
    modifier: Modifier = Modifier,
    onSpeakerClick: (String) -> Unit
) {
    val scrollState = rememberScalingLazyListState()
    ScreenScaffold(
        scrollState = scrollState,
        modifier = modifier,
        scrollIndicator = {
            ScrollIndicator(scrollState, modifier = Modifier.align(Alignment.CenterEnd))
        }
    ) {
        ScalingLazyColumn(
            contentPadding = PaddingValues(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item { ListHeader { Text(modelUi.title) } }
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                ) {
                    CompositionLocalProvider(
                        LocalTextStyle provides MaterialTheme.typography.bodySmall,
                        LocalContentColor provides MaterialTheme.colorScheme.onBackground
                    ) {
                        Text(text = modelUi.startTime)
                        Text(text = modelUi.room)
                    }
                }
            }
            item { ListSubheader { Text("About") } }
            item { MarkdownText(text = modelUi.abstract) }
            item { ListSubheader { Text("Speakers") } }
            items(modelUi.speakers) { speaker ->
                ExtendedActionButton(
                    painter = rememberAsyncImagePainter(model = speaker.url),
                    label = { Text(speaker.name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                    secondaryLabel = {
                        Text(
                            text = speaker.company,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    onClick = { onSpeakerClick(speaker.id) }
                )
            }
        }
    }
}
