package com.paligot.confily.infos.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.paligot.confily.infos.panes.TeamMemberListContent
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.text_error
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TeamMemberListVM(
    onTeamMemberClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TeamMemberListViewModel = koinViewModel()
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is TeamMemberListUiState.Loading -> TeamMemberListContent(
            members = uiState.members,
            modifier = modifier,
            isLoading = true,
            onTeamMemberClicked = onTeamMemberClicked
        )

        is TeamMemberListUiState.Failure -> Text(text = stringResource(Resource.string.text_error))
        is TeamMemberListUiState.Success -> TeamMemberListContent(
            members = uiState.members,
            modifier = modifier,
            isLoading = false,
            onTeamMemberClicked = onTeamMemberClicked
        )
    }
}
