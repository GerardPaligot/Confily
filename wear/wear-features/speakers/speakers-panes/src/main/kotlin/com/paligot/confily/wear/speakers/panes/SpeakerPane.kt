package com.paligot.confily.wear.speakers.panes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.ScrollIndicator
import androidx.wear.compose.material3.Text
import coil3.compose.rememberAsyncImagePainter
import com.paligot.confily.models.ui.SpeakerUi
import com.paligot.confily.models.ui.TalkItemUi
import com.paligot.confily.wear.schedules.ui.ScheduleCardItem
import com.paligot.confily.wear.theme.buttons.IconActionButton
import com.paligot.confily.wear.theme.markdown.MarkdownText
import com.paligot.confily.wear.theme.tags.Tag
import com.paligot.confily.wear.theme.tags.findCategoryImageVector

private const val ContentAlpha = .5f

@Composable
fun SpeakerPane(
    modelUi: SpeakerUi,
    modifier: Modifier = Modifier,
    onSessionClick: (String) -> Unit
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
            item {
                ListHeader {
                    IconActionButton(painter = rememberAsyncImagePainter(model = modelUi.url))
                }
            }
            item { ListHeader { Text(modelUi.name) } }
            if (modelUi.pronouns != null) {
                item {
                    Text(
                        text = modelUi.pronouns!!,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(ContentAlpha)
                    )
                }
            }
            if (modelUi.activity != null) {
                item {
                    Text(
                        text = modelUi.activity!!,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            item { MarkdownText(text = modelUi.bio) }
            items(modelUi.talks) { sessionModelUi: TalkItemUi ->
                ScheduleCardItem(
                    title = sessionModelUi.title,
                    timeSlot = sessionModelUi.slotTime,
                    timeDuration = sessionModelUi.time,
                    bottomBar = {
                        Tag(
                            text = sessionModelUi.category.name,
                            icon = sessionModelUi.category.icon?.findCategoryImageVector()
                        )
                    },
                    onClick = { onSessionClick(sessionModelUi.id) }
                )
            }
        }
    }
}
