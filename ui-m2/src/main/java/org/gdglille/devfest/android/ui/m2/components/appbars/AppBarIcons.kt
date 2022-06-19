package org.gdglille.devfest.android.ui.m2.components.appbars

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.ui.m2.R
import org.gdglille.devfest.android.ui.m2.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.m2.theme.onPrimarySurface

object AppBarIcons {
    @Composable
    fun Back(
        modifier: Modifier = Modifier,
        color: Color = MaterialTheme.colors.onPrimarySurface,
        onClick: () -> Unit
    ) {
        AppBarIcon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = stringResource(id = R.string.action_back),
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
