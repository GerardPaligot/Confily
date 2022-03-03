package com.paligot.conferences.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paligot.conferences.models.UserProfileUi
import com.paligot.conferences.ui.components.appbars.TopAppBar
import com.paligot.conferences.ui.theme.Conferences4HallTheme

enum class Field {
    Email, FirstName, LastName, Company
}

@Composable
fun ProfileInput(
    profile: UserProfileUi,
    modifier: Modifier = Modifier,
    onValueChanged: (Field, String) -> Unit,
    onValidation: () -> Unit,
    onBackClicked: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = "Create your profile",
                navigationIcon = { Back(onClick = onBackClicked) }
            )
        },
    ) {
        val focusManager = LocalFocusManager.current
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(it).fillMaxWidth(),
        ) {
            if (profile.qrCode != null) {
                item {
                    BoxWithConstraints {
                        Image(
                            bitmap = profile.qrCode!!.asImageBitmap(),
                            contentDescription = "Your qrcode to be scanned",
                            modifier = Modifier.size(this.maxWidth * 2/3)
                        )
                    }
                }
            }
            item {
                TextField(
                    value = profile.email,
                    onValueChange = { text -> onValueChanged(Field.Email, text) },
                    label = { Text("Your email address*") },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Email
                    ),
                    singleLine = true,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                TextField(
                    value = profile.firstName,
                    onValueChange = { text -> onValueChanged(Field.FirstName, text) },
                    label = { Text("Your first name*") },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text
                    ),
                    singleLine = true,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                TextField(
                    value = profile.lastName,
                    onValueChange = { text -> onValueChanged(Field.LastName, text) },
                    label = { Text("Your last name*") },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text
                    ),
                    singleLine = true,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                TextField(
                    value = profile.company,
                    onValueChange = { text -> onValueChanged(Field.Company, text) },
                    label = { Text("Your company") },
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                        onValidation()
                    }),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    singleLine = true,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        onValidation()
                    },
                ) {
                    Text("Generate your QR code")
                }
            }
        }
    }
}

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
