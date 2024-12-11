package com.paligot.confily.style.components.adaptive

import androidx.compose.runtime.Composable

@Composable
expect fun BackHandler(
    enabled: Boolean = true,
    onBack: () -> Unit
)
