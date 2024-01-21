package org.gdglille.devfest.android.theme.m3.speakers.feature

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import org.gdglille.devfest.android.theme.m3.speakers.screens.SpeakerDetailOrientable
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.android.theme.m3.style.Scaffold
import org.gdglille.devfest.android.theme.m3.style.appbars.AppBarIcons
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SpeakerDetailOrientableVM(
    speakerId: String,
    onTalkClicked: (id: String) -> Unit,
    onLinkClicked: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (AppBarIcons.() -> Unit)? = null,
    isLandscape: Boolean = false,
    viewModel: SpeakerDetailViewModel = koinViewModel(key = speakerId, parameters = { parametersOf(speakerId) })
) {
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsState()
    Scaffold(
        title = stringResource(id = R.string.screen_speaker_detail),
        navigationIcon = navigationIcon,
        modifier = modifier
    ) {
        when (uiState.value) {
            is SpeakerUiState.Loading -> SpeakerDetailOrientable(
                speaker = (uiState.value as SpeakerUiState.Loading).speaker,
                contentPadding = it,
                onTalkClicked = {},
                onFavoriteClicked = {},
                onLinkClicked = {},
                isLandscape = isLandscape
            )

            is SpeakerUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
            is SpeakerUiState.Success -> SpeakerDetailOrientable(
                speaker = (uiState.value as SpeakerUiState.Success).speaker,
                contentPadding = it,
                onTalkClicked = onTalkClicked,
                onFavoriteClicked = {
                    viewModel.markAsFavorite(context, it)
                },
                onLinkClicked = onLinkClicked,
                isLandscape = isLandscape
            )
        }
    }
}
