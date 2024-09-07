package com.paligot.confily.style.theme.chips

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.style.theme.ConfilyTheme

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
