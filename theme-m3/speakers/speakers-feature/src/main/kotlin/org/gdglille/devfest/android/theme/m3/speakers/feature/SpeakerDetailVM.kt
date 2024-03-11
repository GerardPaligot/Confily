package org.gdglille.devfest.android.theme.m3.speakers.feature

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import org.gdglille.devfest.android.theme.m3.speakers.screens.SpeakerDetailOrientable
import org.gdglille.devfest.android.shared.resources.Resource
import org.gdglille.devfest.android.theme.m3.style.appbars.AppBarIcons
import org.gdglille.devfest.android.shared.resources.text_error
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalResourceApi::class)
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
        parameters = { parametersOf(speakerId) })
) {
    val context = LocalContext.current
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is SpeakerUiState.Loading -> SpeakerDetailOrientable(
            speaker = uiState.speaker,
            onTalkClicked = {},
            onFavoriteClicked = {},
            onLinkClicked = {},
            modifier = modifier,
            navigationIcon = navigationIcon,
            isLandscape = isLandscape,
        )

        is SpeakerUiState.Failure -> Text(text = stringResource(Resource.string.text_error))
        is SpeakerUiState.Success -> SpeakerDetailOrientable(
            speaker = uiState.speaker,
            onTalkClicked = onTalkClicked,
            onFavoriteClicked = {
                viewModel.markAsFavorite(context, it)
            },
            onLinkClicked = onLinkClicked,
            modifier = modifier,
            navigationIcon = navigationIcon,
            isLandscape = isLandscape
        )
    }
}
