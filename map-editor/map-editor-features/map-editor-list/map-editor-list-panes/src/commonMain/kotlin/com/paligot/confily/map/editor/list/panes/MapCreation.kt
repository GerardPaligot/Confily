package com.paligot.confily.map.editor.list.panes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.paligot.confily.map.editor.list.ui.models.MapCreationUi

@Composable
fun MapCreation(
    uiModel: MapCreationUi,
    onNameValueChange: (String) -> Unit,
    onOrderValueChange: (String) -> Unit,
    onCancelClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Image(
                painter = rememberAsyncImagePainter(model = uiModel.planPath),
                contentDescription = null,
                modifier = Modifier.width(350.dp).aspectRatio(ratio = 1f)
            )
        }
        item {
            OutlinedTextField(
                value = uiModel.name,
                onValueChange = onNameValueChange,
                label = { Text(text = "Name") }
            )
        }
        item {
            OutlinedTextField(
                value = uiModel.order,
                onValueChange = onOrderValueChange,
                label = { Text(text = "Order") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onCancelClick,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                ) {
                    Text(text = "Cancel")
                }
                Button(onClick = onSaveClick) {
                    Text(text = "Save")
                }
            }
        }
    }
}
