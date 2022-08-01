package org.gdglille.devfest.android.theme.vitamin.ui.components.talks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import com.decathlon.vitamin.compose.tags.VitaminTags
import org.gdglille.devfest.android.theme.vitamin.ui.R
import org.gdglille.devfest.android.theme.vitamin.ui.components.speakers.SpeakersAvatar
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.models.TalkItemUi

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TalkItem(
    talk: TalkItemUi,
    modifier: Modifier = Modifier,
    onTalkItemClicked: (TalkItemUi) -> Unit,
    onFavoriteClicked: (TalkItemUi) -> Unit
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Card(
            onClick = { onTalkItemClicked(talk) },
            backgroundColor = VitaminTheme.colors.vtmnBackgroundPrimary,
            contentColor = VitaminTheme.colors.vtmnContentPrimary,
            shape = RoundedCornerShape(size = 4.dp)
        ) {
            val contentPadding = 8.dp
            Column(
                modifier = Modifier.fillMaxWidth().padding(contentPadding),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    VitaminTags.DecorativeGravel(
                        label = talk.room,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
                Text(
                    text = talk.title,
                    style = VitaminTheme.typography.body3,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 82.dp - contentPadding)
                )
                if (talk.abstract != "" || talk.speakers.isNotEmpty()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = talk.abstract,
                            style = VitaminTheme.typography.body3,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        SpeakersAvatar(
                            speakersName = talk.speakers,
                            speakersAvatar = talk.speakersAvatar,
                            modifier = Modifier.height(24.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
        }
        IconButton(
            onClick = { onFavoriteClicked(talk) },
            modifier = Modifier.align(Alignment.TopEnd).zIndex(1f)
        ) {
            val favoriteIcon = if (talk.isFavorite) {
                R.drawable.ic_vtmn_heart_3_fill
            } else {
                R.drawable.ic_vtmn_heart_3_line
            }
            val favoriteDescription = if (talk.isFavorite) {
                stringResource(R.string.action_favorites_remove)
            } else {
                stringResource(R.string.action_favorites_add)
            }
            val favoriteColor = if (talk.isFavorite) {
                VitaminTheme.colors.vtmnContentAction
            } else {
                VitaminTheme.colors.vtmnContentInactive
            }
            Icon(
                painter = painterResource(favoriteIcon),
                contentDescription = favoriteDescription,
                tint = favoriteColor
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
