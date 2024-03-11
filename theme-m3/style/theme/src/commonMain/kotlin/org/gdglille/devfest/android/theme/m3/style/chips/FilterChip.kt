package org.gdglille.devfest.android.theme.m3.style.chips

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme

@Composable
fun FilterChip(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    imageVector: ImageVector? = null,
    onClick: (selected: Boolean) -> Unit
) {
    val leadingIcon: (@Composable () -> Unit)? = if (selected) @Composable {
        {
            Icon(
                imageVector = Icons.Filled.Done,
                contentDescription = null,
                modifier = Modifier.size(FilterChipDefaults.IconSize)
            )
        }
    } else if (imageVector != null) {
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
    // TODO Don't know why for now but FilterChip throw a NoSuchMethodError exception.
    androidx.compose.material3.AssistChip(
        onClick = { onClick(selected.not()) },
        label = { Text(label) },
        modifier = modifier,
        leadingIcon = leadingIcon
    )
}

@Preview
@Composable
private fun FilterChipPreview2() {
    Conferences4HallTheme {
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
