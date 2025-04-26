package com.paligot.confily.networking.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.paligot.confily.networking.panes.EmptyNetworkingContent
import com.paligot.confily.networking.panes.MyProfileContent
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.text_error
import com.paligot.confily.resources.text_loading
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MyProfileCompactVM(
    onEditInformation: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MyProfileViewModel = koinViewModel()
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is MyProfileUiState.Loading -> Text(text = stringResource(Resource.string.text_loading))
        is MyProfileUiState.Failure -> Text(text = stringResource(Resource.string.text_error))
        is MyProfileUiState.Success -> {
            val profileUi = uiState.profile
            if (profileUi.qrCode == null) {
                EmptyNetworkingContent(modifier = modifier)
            } else {
                MyProfileContent(
                    profileUi = profileUi,
                    modifier = modifier,
                    onEditInformation = onEditInformation
                )
            }
        }
    }
}
