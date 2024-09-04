package org.gdglille.devfest.android.theme.m3.networking.feature

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.paligot.confily.networking.panes.EmptyNetworkingScreen
import com.paligot.confily.networking.panes.MyProfileScreen
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.text_error
import com.paligot.confily.resources.text_loading
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalResourceApi::class)
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
                EmptyNetworkingScreen(modifier = modifier)
            } else {
                MyProfileScreen(
                    profileUi = profileUi,
                    modifier = modifier,
                    onEditInformation = onEditInformation
                )
            }
        }
    }
}
