package com.paligot.confily.infos.panes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.paligot.confily.infos.ui.models.TeamMemberItemUi
import com.paligot.confily.style.components.placeholder.placeholder
import com.paligot.confily.style.speakers.items.LargeSpeakerItem
import com.paligot.confily.style.theme.SpacingTokens
import com.paligot.confily.style.theme.toDp
import kotlinx.collections.immutable.ImmutableList

@Composable
fun TeamMemberListContent(
    members: ImmutableList<TeamMemberItemUi>,
    onTeamMemberClicked: (id: String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    isLoading: Boolean = false
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 130.dp),
        modifier = modifier
            .padding(top = contentPadding.calculateTopPadding())
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.MediumSpacing.toDp()),
        horizontalArrangement = Arrangement.spacedBy(SpacingTokens.MediumSpacing.toDp()),
        contentPadding = PaddingValues(
            vertical = SpacingTokens.ExtraLargeSpacing.toDp(),
            horizontal = SpacingTokens.MediumSpacing.toDp()
        )
    ) {
        items(members, key = { it.id }) { member ->
            LargeSpeakerItem(
                name = member.displayName,
                description = member.role,
                url = member.url ?: "",
                onClick = { onTeamMemberClicked(member.id) },
                modifier = Modifier.placeholder(isLoading)
            )
        }
    }
}
