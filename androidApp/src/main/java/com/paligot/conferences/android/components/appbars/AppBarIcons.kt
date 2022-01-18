package com.paligot.conferences.android.components.appbars

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
import com.paligot.conferences.android.theme.Conferences4HallTheme

object AppBarIcons {
  @Composable
  fun Back(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onPrimary,
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
  color: Color = MaterialTheme.colors.onPrimary,
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
