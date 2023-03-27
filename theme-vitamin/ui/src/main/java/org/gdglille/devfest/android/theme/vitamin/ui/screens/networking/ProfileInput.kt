@file:Suppress("MatchingDeclarationName")

package org.gdglille.devfest.android.theme.vitamin.ui.screens.networking

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decathlon.vitamin.compose.buttons.VitaminButtons
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import com.decathlon.vitamin.compose.textinputs.VitaminTextInputs
import org.gdglille.devfest.android.theme.vitamin.ui.components.appbars.ExtendedTopAppBar
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.Field
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.models.UserProfileUi
import com.decathlon.vitamin.compose.foundation.R as RVitamin

@Suppress("LongMethod")
@Composable
fun ProfileInput(
    profile: UserProfileUi,
    onValueChanged: (Field, String) -> Unit,
    onValidation: () -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            ExtendedTopAppBar(
                title = stringResource(id = R.string.screen_profile),
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            painter = painterResource(RVitamin.drawable.ic_vtmn_arrow_left_line),
                            contentDescription = stringResource(id = R.string.action_back)
                        )
                    }
                },
                backgroundColor = VitaminTheme.colors.vtmnBackgroundBrandPrimary,
                contentColor = VitaminTheme.colors.vtmnContentPrimaryReversed
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
                VitaminTextInputs.Outlined(
                    value = profile.firstName,
                    label = stringResource(id = R.string.input_firstname),
                    onValueChange = { text -> onValueChanged(Field.FirstName, text) },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text
                    ),
                    singleLine = true,
                    maxLines = 1
                )
            }
            item {
                VitaminTextInputs.Outlined(
                    value = profile.lastName,
                    label = stringResource(id = R.string.input_lastname),
                    onValueChange = { text -> onValueChanged(Field.LastName, text) },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text
                    ),
                    singleLine = true,
                    maxLines = 1
                )
            }
            item {
                VitaminTextInputs.Outlined(
                    value = profile.email,
                    label = stringResource(id = R.string.input_email),
                    onValueChange = { text -> onValueChanged(Field.Email, text) },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Email
                    ),
                    singleLine = true,
                    maxLines = 1
                )
            }
            item {
                VitaminTextInputs.Outlined(
                    value = profile.company,
                    label = stringResource(id = R.string.input_company),
                    onValueChange = { text -> onValueChanged(Field.Company, text) },
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                        onValidation()
                    }),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    singleLine = true,
                    maxLines = 1
                )
            }
            item {
                VitaminButtons.Primary(
                    text = stringResource(id = R.string.action_generate_qrcode),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    focusManager.clearFocus()
                    onValidation()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
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
