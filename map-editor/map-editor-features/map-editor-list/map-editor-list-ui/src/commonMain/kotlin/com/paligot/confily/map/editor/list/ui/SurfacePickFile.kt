package com.paligot.confily.map.editor.list.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun SurfacePickFile(
    label: String,
    onPickFiles: (List<String>) -> Unit,
    modifier: Modifier = Modifier
)
