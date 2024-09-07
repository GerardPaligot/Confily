package com.paligot.confily.style.theme.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.style.theme.ConfilyTheme

@Preview
@Composable
private fun IconButtonPreview() {
    ConfilyTheme {
        IconButton(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            onClick = {}
        )
    }
}
