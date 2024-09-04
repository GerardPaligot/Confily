package org.gdglille.devfest.android.theme.m3.networking.feature

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.paligot.confily.models.ui.VCardModel
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.screen_qrcode_scanner
import com.paligot.confily.style.components.permissions.FeatureThatRequiresCameraPermission
import com.paligot.confily.style.theme.appbars.TopAppBar
import org.gdglille.devfest.android.theme.m3.networking.ui.VCardCameraPreview
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun VCardQrCodeScanner(
    navigateToSettingsScreen: () -> Unit,
    onQrCodeDetected: (VCardModel) -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = stringResource(Resource.string.screen_qrcode_scanner),
                navigationIcon = { Back(onClick = onBackClicked) }
            )
        },
        content = {
            Box(modifier = Modifier.padding(it)) {
                FeatureThatRequiresCameraPermission(
                    navigateToSettingsScreen = navigateToSettingsScreen,
                    onRefusePermissionClicked = onBackClicked,
                    content = {
                        VCardCameraPreview(onQrCodeDetected = { vcards ->
                            onQrCodeDetected(vcards.first())
                        })
                    }
                )
            }
        }
    )
}
