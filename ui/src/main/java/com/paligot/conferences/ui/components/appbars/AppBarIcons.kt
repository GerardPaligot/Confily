package com.paligot.conferences.ui.components.appbars

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.conferences.ui.theme.Conferences4HallTheme
import com.paligot.conferences.ui.theme.onPrimarySurface

object AppBarIcons {
    @Composable
    fun Back(
        modifier: Modifier = Modifier,
        color: Color = MaterialTheme.colors.onPrimarySurface,
        onClick: () -> Unit
    ) {
        AppBarIcon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Back",
            modifier = modifier,
            color = color,
            onClick = onClick
        )
    }
}

@Composable
internal fun AppBarIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onPrimarySurface,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = color
        )
    }
}

@Preview
@Composable
fun AppBarIconPreview() {
    Conferences4HallTheme {
        AppBarIcons.Back {
        }
    }
}
