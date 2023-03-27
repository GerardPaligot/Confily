package org.gdglille.devfest.android.theme.vitamin.ui.screens.networking

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decathlon.vitamin.compose.dividers.VitaminDividers
import com.decathlon.vitamin.compose.modals.VitaminModals
import kotlinx.collections.immutable.ImmutableList
import org.gdglille.devfest.android.theme.vitamin.ui.components.users.UserItem
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.models.NetworkingUi
import org.gdglille.devfest.models.UserNetworkingUi

@Composable
fun Contacts(
    users: ImmutableList<UserNetworkingUi>,
    onNetworkDeleted: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val openDialog = remember { mutableStateOf(false) }
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(start = 16.dp)
    ) {
        items(users) { user ->
            UserItem(
                user = user,
                onClick = { openDialog.value = true }
            )
            VitaminDividers.FullBleed()
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

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview
@Composable
fun ContactsPreview() {
    Conferences4HallTheme {
        Scaffold {
            Contacts(
                users = NetworkingUi.fake.users,
                onNetworkDeleted = {}
            )
        }
    }
}
