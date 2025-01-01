package com.paligot.confily.infos.panes

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.paligot.confily.infos.ui.team.TeamDetailSection
import com.paligot.confily.models.ui.TeamMemberUi
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.screen_team
import com.paligot.confily.style.theme.Scaffold
import com.paligot.confily.style.theme.SpacingTokens
import com.paligot.confily.style.theme.toDp
import org.jetbrains.compose.resources.stringResource

@Composable
fun TeamMemberPane(
    uiModel: TeamMemberUi,
    onLinkClicked: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Scaffold(
        title = stringResource(Resource.string.screen_team),
        modifier = modifier
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                vertical = SpacingTokens.ExtraLargeSpacing.toDp(),
                horizontal = SpacingTokens.LargeSpacing.toDp()
            ),
            modifier = Modifier.padding(it)
        ) {
            item {
                TeamDetailSection(
                    uiModel = uiModel,
                    onLinkClicked = onLinkClicked,
                    isLoading = isLoading
                )
            }
        }
    }
}
