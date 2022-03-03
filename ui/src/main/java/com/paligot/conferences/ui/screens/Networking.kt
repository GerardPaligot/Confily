package com.paligot.conferences.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paligot.conferences.models.NetworkingUi
import com.paligot.conferences.ui.components.users.EmailItem
import com.paligot.conferences.ui.theme.Conferences4HallTheme

@Composable
fun Networking(
    emails: List<String>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(emails) { index, email ->
            EmailItem(email = email)
            if (index < emails.size - 1) {
                Divider()
            }
        }
    }
}

@Preview
@Composable
fun NetworkingPreview() {
    Conferences4HallTheme {
        Scaffold {
            Networking(emails = NetworkingUi.fake.emails)
        }
    }
}
