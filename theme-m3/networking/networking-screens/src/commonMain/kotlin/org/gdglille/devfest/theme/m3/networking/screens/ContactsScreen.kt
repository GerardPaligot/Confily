package org.gdglille.devfest.theme.m3.networking.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.paligot.confily.models.ui.UserNetworkingUi
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.action_submit_accept
import com.paligot.confily.resources.action_submit_deny
import com.paligot.confily.resources.text_networking_ask_to_delete
import kotlinx.collections.immutable.ImmutableList
import org.gdglille.devfest.theme.m3.style.networking.UserItem
import org.jetbrains.compose.resources.stringResource

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
                    company = user.company,
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
