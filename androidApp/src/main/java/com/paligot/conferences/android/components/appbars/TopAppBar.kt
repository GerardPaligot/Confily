package com.paligot.conferences.android.components.appbars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paligot.conferences.android.theme.Conferences4HallTheme

@Composable
fun TopAppBar(
  title: String,
  modifier: Modifier = Modifier,
  navigationIcon: @Composable (AppBarIcons.() -> Unit)? = null
) {
  androidx.compose.material.TopAppBar(
    title = { Text(text = title) },
    modifier = modifier,
    navigationIcon = navigationIcon.takeOrNull(),
  )
}

internal fun (@Composable AppBarIcons.() -> Unit)?.takeOrNull(): (@Composable () -> Unit)? {
  if (this == null) return null
  return {
    AppBarIcons.this()
  }
}

@Preview
@Composable
fun TopAppBarPeview() {
  Conferences4HallTheme {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      TopAppBar(
        title = "Speakers"
      )
      TopAppBar(
        title = "Speakers",
        navigationIcon = { Back {  } }
      )
    }
  }
}
