package com.paligot.confily.infos.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.paligot.confily.infos.panes.TeamMemberPane
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.text_error
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TeamMemberVM(
    onLinkClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TeamMemberViewModel = koinViewModel()
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is TeamMemberUiState.Loading -> TeamMemberPane(
            uiModel = uiState.member,
            onLinkClicked = onLinkClicked,
            modifier = modifier,
            isLoading = true
        )

        is TeamMemberUiState.Failure -> Text(text = stringResource(Resource.string.text_error))
        is TeamMemberUiState.Success -> TeamMemberPane(
            uiModel = uiState.member,
            onLinkClicked = onLinkClicked,
            modifier = modifier,
            isLoading = false
        )
    }
}
