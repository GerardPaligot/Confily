package org.gdglille.devfest.android.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.components.users.UserItem
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.R
import org.gdglille.devfest.models.NetworkingUi
import org.gdglille.devfest.models.UserNetworkingUi

@Composable
fun Networking(
    users: List<UserNetworkingUi>,
    modifier: Modifier = Modifier,
    onNetworkDeleted: (String) -> Unit
) {
    if (users.isEmpty()) {
        EmptyNetworking(modifier = modifier)
    } else {
        val openDialog = remember { mutableStateOf(false) }
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(users) { index, user ->
                UserItem(
                    user = user,
                    modifier = Modifier.clickable { openDialog.value = true }
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
                                Text(stringResource(R.string.action_submit_accept))
                            }
                        },
                        dismissButton = {
                            Button(onClick = { openDialog.value = false }) {
                                Text(stringResource(R.string.action_submit_deny))
                            }
                        },
                        text = {
                            Text(
                                text =
                                stringResource(
                                    R.string.text_networking_ask_to_delete,
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

@Composable
fun EmptyNetworking(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.text_networking_empty),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            textAlign = TextAlign.Center
        )
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview
@Composable
fun NetworkingPreview() {
    Conferences4HallTheme {
        Scaffold {
            Networking(users = NetworkingUi.fake.users) {}
        }
    }
}
