package org.gdglille.devfest.android.components.users

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import org.gdglille.devfest.android.ui.resources.Field

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileInputField(
    @StringRes label: Int,
    value: String,
    field: Field,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    onDone: (KeyboardActionScope.() -> Unit)? = null,
    onValueChanged: (Field, String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = { text -> onValueChanged(field, text) },
        label = { Text(text = stringResource(id = label)) },
        keyboardActions = KeyboardActions(onDone = onDone),
        keyboardOptions = KeyboardOptions(
            imeAction = if (onDone != null) ImeAction.Done else ImeAction.Next,
            keyboardType = keyboardType
        ),
        singleLine = true,
        maxLines = 1,
        modifier = modifier.fillMaxWidth()
    )
}
