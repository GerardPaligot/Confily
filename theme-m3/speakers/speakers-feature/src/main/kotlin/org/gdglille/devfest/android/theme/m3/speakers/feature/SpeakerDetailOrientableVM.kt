package org.gdglille.devfest.android.theme.m3.speakers.feature

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import org.gdglille.devfest.android.theme.m3.speakers.screens.SpeakerDetailOrientable
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.android.theme.m3.style.appbars.TopAppBar
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeakerDetailOrientableVM(
    speakerId: String,
    onTalkClicked: (id: String) -> Unit,
    onLinkClicked: (url: String) -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SpeakerDetailViewModel = koinViewModel(parameters = { parametersOf(speakerId) })
) {
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.screen_speaker_detail),
                navigationIcon = { Back(onClick = onBackClicked) },
                scrollBehavior = scrollBehavior
            )
        },
        content = {
            when (uiState.value) {
                is SpeakerUiState.Loading -> SpeakerDetailOrientable(
                    speaker = (uiState.value as SpeakerUiState.Loading).speaker,
                    contentPadding = it,
                    onTalkClicked = {},
                    onFavoriteClicked = {},
                    onLinkClicked = {}
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
                )
            }
        }
    )
}
