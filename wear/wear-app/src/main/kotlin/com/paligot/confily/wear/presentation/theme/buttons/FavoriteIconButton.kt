package com.paligot.confily.wear.presentation.theme.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.IconButton

@Composable
fun FavoriteIconButton(
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (selected) {
                Icons.Filled.Star
            } else {
                Icons.Outlined.Star
            },
            contentDescription = null
        )
    }
}
