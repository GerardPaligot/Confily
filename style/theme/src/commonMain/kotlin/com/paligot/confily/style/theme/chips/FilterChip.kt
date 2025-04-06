package com.paligot.confily.style.theme.chips

import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

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
