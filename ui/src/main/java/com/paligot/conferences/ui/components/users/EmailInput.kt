package com.paligot.conferences.ui.components.users

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paligot.conferences.ui.theme.Conferences4HallTheme

@Composable
fun EmailInput(
    value: String,
    hasQrCode: Boolean,
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colors.onBackground,
    onValueChanged: (String) -> Unit,
    onValidation: () -> Unit,
    onQrCodeClicked: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChanged,
            label = { Text("Your email address") },
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
                onValidation()
            }),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Email
            ),
            singleLine = true,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth().weight(1f),
            shape = CircleShape,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = contentColor,
                unfocusedLabelColor = contentColor
            )
        )
        IconButton(onClick = {
            focusManager.clearFocus()
            onValidation()
        }) {
            Icon(
                imageVector = Icons.Filled.Save,
                contentDescription = "Save email to generate qrcode",
                tint = contentColor
            )
        }
        IconButton(onClick = { onQrCodeClicked() }, enabled = hasQrCode) {
            Icon(
                imageVector = Icons.Filled.QrCode,
                contentDescription = "Click to show your qrcode of your email address",
                tint = contentColor
            )
        }
    }
}

@Preview
@Composable
fun EmailInputPreview() {
    Conferences4HallTheme {
        Scaffold {
            EmailInput(
                value = "",
                hasQrCode = true,
                onValueChanged = {},
                onValidation = {},
                onQrCodeClicked = {}
            )
        }
    }
}
