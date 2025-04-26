package com.paligot.confily.speakers.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.text_error
import com.paligot.confily.speakers.panes.SpeakerDetailPane
import com.paligot.confily.style.theme.appbars.AppBarIcons
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SpeakerDetailVM(
    speakerId: String,
    onTalkClicked: (id: String) -> Unit,
    onLinkClicked: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (AppBarIcons.() -> Unit)? = null,
    isLandscape: Boolean = false,
    viewModel: SpeakerDetailViewModel = koinViewModel(
        key = speakerId,
        parameters = { parametersOf(speakerId) }
    )
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is SpeakerUiState.Loading -> SpeakerDetailPane(
            speaker = uiState.speaker,
            onTalkClicked = {},
            onFavoriteClicked = {},
            onLinkClicked = {},
            modifier = modifier,
            navigationIcon = navigationIcon,
            isLandscape = isLandscape
        )

        is SpeakerUiState.Failure -> Text(text = stringResource(Resource.string.text_error))
        is SpeakerUiState.Success -> SpeakerDetailPane(
            speaker = uiState.speaker,
            onTalkClicked = onTalkClicked,
            onFavoriteClicked = viewModel::markAsFavorite,
            onLinkClicked = onLinkClicked,
            modifier = modifier,
            navigationIcon = navigationIcon,
            isLandscape = isLandscape
        )
    }
}
