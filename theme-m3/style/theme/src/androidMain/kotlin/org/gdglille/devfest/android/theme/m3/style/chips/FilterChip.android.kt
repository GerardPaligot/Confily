package org.gdglille.devfest.android.theme.m3.style.chips

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme

@Preview
@Composable
private fun FilterChipPreview() {
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
