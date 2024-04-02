package org.gdglille.devfest.android.theme.m3.networking.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import org.gdglille.devfest.android.shared.resources.Resource
import org.gdglille.devfest.android.shared.resources.action_submit_accept
import org.gdglille.devfest.android.shared.resources.action_submit_deny
import org.gdglille.devfest.android.shared.resources.text_networking_ask_to_delete
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.networking.UserItem
import org.gdglille.devfest.models.ui.NetworkingUi
import org.gdglille.devfest.models.ui.UserNetworkingUi
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ContactsScreen(
    users: ImmutableList<UserNetworkingUi>,
    onNetworkDeleted: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (users.isEmpty()) {
        EmptyContactsScreen(modifier = modifier)
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
                    company = user.email,
                    onClick = { openDialog.value = true }
                )
                if (index < users.size - 1) {
                    Divider()
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Preview
@Composable
private fun ContactsPreview() {
    Conferences4HallTheme {
        Scaffold {
            ContactsScreen(
                users = NetworkingUi.fake.users,
                onNetworkDeleted = {}
            )
        }
    }
}
