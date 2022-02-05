package com.paligot.conferences.ui.components.users

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paligot.conferences.ui.theme.Conferences4HallTheme

@Composable
fun EmailItem(
    email: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onBackground,
    style: TextStyle = MaterialTheme.typography.body1,
    contentPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 16.dp)
) {
    Text(
        text = email,
        modifier = modifier.fillMaxWidth().padding(contentPadding),
        color = color,
        style = style
    )
}

@Preview
@Composable
fun EmailItemPreview() {
    Conferences4HallTheme {
        Scaffold {
            LazyColumn() {
                item {
                    EmailItem(
                        email = "gerard@gdglille.org"
                    )
                    Divider(color = MaterialTheme.colors.onBackground)
                }
                item {
                    EmailItem(
                        email = "gerard@gdglille.org"
                    )
                    Divider(color = MaterialTheme.colors.onBackground)
                }
                item {
                    EmailItem(
                        email = "gerard@gdglille.org"
                    )
                }
            }
        }
    }
}
