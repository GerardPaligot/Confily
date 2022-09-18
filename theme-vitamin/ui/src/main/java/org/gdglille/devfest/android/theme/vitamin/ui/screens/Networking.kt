package org.gdglille.devfest.android.theme.vitamin.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.decathlon.vitamin.compose.modals.VitaminModals
import org.gdglille.devfest.android.theme.vitamin.ui.R
import org.gdglille.devfest.android.theme.vitamin.ui.components.users.UserItem
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
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
                    onClick = { openDialog.value = true }
                )
                if (index < users.size - 1) {
                    Divider()
                }
                if (openDialog.value) {
                    VitaminModals.Primary(
                        content = {
                            Text(
                                text = stringResource(
                                    R.string.text_networking_ask_to_delete,
                                    "${user.firstName} ${user.lastName}"
                                )
                            )
                        },
                        onDismissRequest = { openDialog.value = false },
                        positiveButton = {
                            Primary(text = stringResource(R.string.action_submit_accept)) {
                                onNetworkDeleted(user.email)
                                openDialog.value = false
                            }
                        },
                        negativeButton = {
                            Primary(text = stringResource(R.string.action_submit_deny)) {
                                openDialog.value = false
                            }
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
