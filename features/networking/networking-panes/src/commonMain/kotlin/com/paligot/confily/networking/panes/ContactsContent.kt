package com.paligot.confily.networking.panes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.paligot.confily.networking.ui.models.NetworkingUi
import com.paligot.confily.networking.ui.models.UserNetworkingUi
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.action_submit_accept
import com.paligot.confily.resources.action_submit_deny
import com.paligot.confily.resources.text_networking_ask_to_delete
import com.paligot.confily.style.networking.UserItem
import com.paligot.confily.style.theme.ConfilyTheme
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ContactsContent(
    users: ImmutableList<UserNetworkingUi>,
    onNetworkDeleted: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (users.isEmpty()) {
        EmptyContactsContent(modifier = modifier)
    } else {
        val openDialog = remember { mutableStateOf(false) }
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(users) { index, user ->
                UserItem(
                    displayName = "${user.firstName} ${user.lastName}",
                    email = user.email,
                    company = user.company,
                    onClick = { openDialog.value = true }
                )
                if (index < users.size - 1) {
                    HorizontalDivider()
                }
                if (openDialog.value) {
                    AlertDialog(
                        onDismissRequest = { openDialog.value = false },
                        confirmButton = {
                            Button(
                                onClick = {
                                    onNetworkDeleted(user.email)
                                    openDialog.value = false
                                }
                            ) {
                                Text(stringResource(Resource.string.action_submit_accept))
                            }
                        },
                        dismissButton = {
                            Button(onClick = { openDialog.value = false }) {
                                Text(stringResource(Resource.string.action_submit_deny))
                            }
                        },
                        text = {
                            Text(
                                text =
                                stringResource(
                                    Resource.string.text_networking_ask_to_delete,
                                    "${user.firstName} ${user.lastName}"
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Preview
@Composable
private fun ContactsContentPreview() {
    ConfilyTheme {
        Scaffold {
            ContactsContent(
                users = NetworkingUi.fake.users,
                onNetworkDeleted = {}
            )
        }
    }
}
