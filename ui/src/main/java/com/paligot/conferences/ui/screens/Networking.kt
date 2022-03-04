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
import com.paligot.conferences.models.UserNetworkingUi
import com.paligot.conferences.ui.components.users.UserItem
import com.paligot.conferences.ui.theme.Conferences4HallTheme

@Composable
fun Networking(
    users: List<UserNetworkingUi>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(users) { index, user ->
            UserItem(user = user)
            if (index < users.size - 1) {
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
            Networking(users = NetworkingUi.fake.users)
        }
    }
}
