package org.gdglille.devfest.android.theme.vitamin.ui.components.talks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import org.gdglille.devfest.android.theme.vitamin.ui.components.tags.DecorativeTag
import org.gdglille.devfest.android.theme.vitamin.ui.components.tags.LevelTag
import org.gdglille.devfest.android.theme.vitamin.ui.components.tags.UnStyledTag
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.models.TalkItemUi
import com.decathlon.vitamin.compose.foundation.R as RVitamin

const val ShortTalk = 30

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TalkItem(
    talk: TalkItemUi,
    onTalkItemClicked: (TalkItemUi) -> Unit,
    onFavoriteClicked: (TalkItemUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Card(
            onClick = { onTalkItemClicked(talk) },
            backgroundColor = VitaminTheme.colors.vtmnBackgroundPrimary,
            contentColor = VitaminTheme.colors.vtmnContentPrimary,
            shape = RoundedCornerShape(size = 8.dp),
            elevation = 2.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                if (talk.category.color != null || talk.level != null) {
                    Column {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            talk.category.color?.let {
                                DecorativeTag(category = talk.category)
                            }
                            talk.level?.let {
                                LevelTag(level = it)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                Text(
                    text = talk.title,
                    style = VitaminTheme.typography.subtitle1,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (talk.speakers.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.text_speakers, talk.speakers.joinToString(", ")),
                        style = VitaminTheme.typography.body3,
                        color = VitaminTheme.colors.vtmnContentSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    UnStyledTag(
                        text = talk.room,
                        icon = RVitamin.drawable.ic_vtmn_map_pin_line,
                        style = VitaminTheme.typography.body3
                    )
                    UnStyledTag(
                        text = stringResource(R.string.text_schedule_minutes, talk.timeInMinutes.toString()),
                        icon = if (talk.timeInMinutes <= ShortTalk) {
                            RVitamin.drawable.ic_vtmn_flashlight_line
                        } else {
                            RVitamin.drawable.ic_vtmn_timer_line
                        },
                        style = VitaminTheme.typography.body3
                    )
                }
            }
        }
        IconButton(
            onClick = { onFavoriteClicked(talk) },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .zIndex(1f),
        ) {
            val favoriteIcon = if (talk.isFavorite) {
                RVitamin.drawable.ic_vtmn_star_fill
            } else {
                RVitamin.drawable.ic_vtmn_star_line
            }
            val favoriteDescription = if (talk.isFavorite) {
                stringResource(R.string.action_favorites_remove)
            } else {
                stringResource(R.string.action_favorites_add)
            }
            val favoriteColor = if (talk.isFavorite) {
                VitaminTheme.colors.vtmnContentActive
            } else {
                VitaminTheme.colors.vtmnContentInactive
            }
            Icon(
                painter = painterResource(favoriteIcon),
                contentDescription = favoriteDescription,
                tint = favoriteColor,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@Preview
@Composable
fun TalkItemPreview() {
    Conferences4HallTheme {
        TalkItem(
            talk = TalkItemUi.fake,
            onTalkItemClicked = {},
            onFavoriteClicked = {}
        )
    }
}
