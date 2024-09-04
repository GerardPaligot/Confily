package org.gdglille.devfest.android.theme.m3.networking.feature

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.text_error
import com.paligot.confily.resources.text_loading
import org.gdglille.devfest.theme.m3.networking.screens.ProfileInputScreen
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun ProfileInputVM(
    onBackClicked: () -> Unit,
    onProfileCreated: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileInputViewModel = koinViewModel()
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is ProfileInputUiState.Loading -> Text(text = stringResource(Resource.string.text_loading))
        is ProfileInputUiState.Failure -> Text(text = stringResource(Resource.string.text_error))
        is ProfileInputUiState.Success -> ProfileInputScreen(
            profile = uiState.profile,
            modifier = modifier,
            onBackClicked = onBackClicked,
            onValueChanged = viewModel::fieldChanged,
            onValidation = {
                viewModel.saveProfile()
                onProfileCreated()
            }
        )
    }
}
