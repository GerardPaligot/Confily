package com.paligot.confily.style.theme.chips

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.paligot.confily.style.theme.ConfilyTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FilterChip(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    imageVector: ImageVector? = null,
    onClick: (selected: Boolean) -> Unit
) {
    val leadingIcon: (@Composable () -> Unit)? = if (imageVector != null) {
        @Composable {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                modifier = Modifier.size(FilterChipDefaults.IconSize)
            )
        }
    } else {
        null
    }
    androidx.compose.material3.FilterChip(
        onClick = { onClick(selected.not()) },
        label = { Text(label) },
        modifier = modifier,
        selected = selected,
        leadingIcon = leadingIcon
    )
}

@Preview
@Composable
private fun FilterChipPreview() {
    ConfilyTheme {
        Surface {
            Column {
                FilterChip(
                    label = "Mobile",
                    imageVector = Icons.Default.Add,
                    selected = false,
                    onClick = {
                    }
                )
                FilterChip(
                    label = "Mobile",
                    imageVector = Icons.Default.Add,
                    selected = false,
                    onClick = {
                    }
                )
            }
        }
    }
}
