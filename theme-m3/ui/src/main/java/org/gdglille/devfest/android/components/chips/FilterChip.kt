package org.gdglille.devfest.android.components.chips

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.models.CategoryUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChip(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    imageVector: ImageVector? = null,
    onClick: (selected: Boolean) -> Unit
) {
    androidx.compose.material3.FilterChip(
        onClick = { onClick(selected.not()) },
        label = { Text(label) },
        selected = selected,
        modifier = modifier,
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = null,
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else if (imageVector != null) {
            {
                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
    )
}

@Preview
@Composable
private fun FilterChipPreview() {
    Conferences4HallTheme {
        FilterChip(
            label = CategoryUi.fake.name,
            selected = false,
            onClick = {
            }
        )
    }
}
