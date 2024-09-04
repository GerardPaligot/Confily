package com.paligot.confily.networking.panes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.paligot.confily.models.ui.Field
import com.paligot.confily.models.ui.UserProfileUi
import com.paligot.confily.networking.ui.ProfileInputField
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.action_generate_qrcode
import com.paligot.confily.resources.input_company
import com.paligot.confily.resources.input_email
import com.paligot.confily.resources.input_firstname
import com.paligot.confily.resources.input_lastname
import com.paligot.confily.resources.screen_profile
import com.paligot.confily.resources.text_networking_consents
import com.paligot.confily.style.theme.appbars.TopAppBar
import org.jetbrains.compose.resources.stringResource

@ExperimentalMaterial3Api
@Composable
fun ProfileInputScreen(
    profile: UserProfileUi,
    onValueChanged: (Field, String) -> Unit,
    onValidation: () -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isFormValid = isProfileInputFormValid(profile)
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = stringResource(Resource.string.screen_profile),
                navigationIcon = { Back(onClick = onBackClicked) }
            )
        }
    ) {
        val focusManager = LocalFocusManager.current
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = it,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 36.dp)
                .fillMaxWidth()
        ) {
            item {
                ProfileInputField(
                    label = Resource.string.input_email,
                    value = profile.email,
                    field = Field.Email,
                    onValueChanged = onValueChanged,
                    keyboardType = KeyboardType.Email
                )
            }
            item {
                ProfileInputField(
                    label = Resource.string.input_firstname,
                    value = profile.firstName,
                    field = Field.FirstName,
                    onValueChanged = onValueChanged
                )
            }
            item {
                ProfileInputField(
                    label = Resource.string.input_lastname,
                    value = profile.lastName,
                    field = Field.LastName,
                    onValueChanged = onValueChanged
                )
            }
            item {
                ProfileInputField(
                    label = Resource.string.input_company,
                    value = profile.company,
                    field = Field.Company,
                    onValueChanged = onValueChanged,
                    onDone = {
                        focusManager.clearFocus()
                        onValidation()
                    }
                )
            }
            item {
                Text(stringResource(Resource.string.text_networking_consents))
            }
            item {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isFormValid,
                    onClick = {
                        focusManager.clearFocus()
                        onValidation()
                    }
                ) {
                    Text(text = stringResource(Resource.string.action_generate_qrcode))
                }
            }
        }
    }
}

fun isProfileInputFormValid(profile: UserProfileUi): Boolean =
    profile.email.isNotEmpty() && profile.firstName.isNotEmpty() && profile.lastName.isNotEmpty()
