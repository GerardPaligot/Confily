package org.gdglille.devfest.android.screens.networking

import android.annotation.SuppressLint
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.components.appbars.TopAppBar
import org.gdglille.devfest.android.components.users.ProfileInputField
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.Field
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.models.UserProfileUi

@ExperimentalMaterial3Api
@Composable
fun ProfileInput(
    profile: UserProfileUi,
    onValueChanged: (Field, String) -> Unit,
    onValidation: () -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isFormValid = isProfileInputFormValid(profile)
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.screen_profile),
                navigationIcon = { Back(onClick = onBackClicked) }
            )
        },
    ) {
        val focusManager = LocalFocusManager.current
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = it,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 36.dp)
                .fillMaxWidth(),
        ) {
            item {
                ProfileInputField(
                    label = R.string.input_email,
                    value = profile.email,
                    field = Field.Email,
                    onValueChanged = onValueChanged,
                    keyboardType = KeyboardType.Email
                )
            }
            item {
                ProfileInputField(
                    label = R.string.input_firstname,
                    value = profile.firstName,
                    field = Field.FirstName,
                    onValueChanged = onValueChanged
                )
            }
            item {
                ProfileInputField(
                    label = R.string.input_lastname,
                    value = profile.lastName,
                    field = Field.LastName,
                    onValueChanged = onValueChanged
                )
            }
            item {
                ProfileInputField(
                    label = R.string.input_company,
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
                Button(
                    enabled = isFormValid,
                    onClick = {
                        focusManager.clearFocus()
                        onValidation()
                    },
                ) {
                    Text(text = stringResource(id = R.string.action_generate_qrcode))
                }
            }
        }
    }
}

fun isProfileInputFormValid(profile: UserProfileUi): Boolean =
    profile.email.isNotEmpty() && profile.firstName.isNotEmpty() && profile.lastName.isNotEmpty()

@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun EmailInputPreview() {
    Conferences4HallTheme {
        Scaffold {
            ProfileInput(
                profile = UserProfileUi.fake,
                onValueChanged = { _, _ -> },
                onValidation = {},
                onBackClicked = {}
            )
        }
    }
}
