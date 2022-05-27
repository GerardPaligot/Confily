package org.gdglille.devfest.android.components.users

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.R
import org.gdglille.devfest.models.NetworkingUi
import org.gdglille.devfest.models.UserNetworkingUi

@Composable
fun UserItem(
    user: UserNetworkingUi,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onBackground,
    nameStyle: TextStyle = MaterialTheme.typography.body1,
    metaStyle: TextStyle = MaterialTheme.typography.caption,
    contentPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
    onClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.weight(1f)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = user.firstName,
                    color = color,
                    style = nameStyle
                )
                Text(
                    text = user.lastName,
                    color = color,
                    style = nameStyle
                )
            }
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = user.email,
                    color = color.copy(alpha = LocalContentAlpha.current),
                    style = metaStyle
                )
                Text(
                    text = user.company,
                    color = color.copy(alpha = LocalContentAlpha.current),
                    style = metaStyle
                )
            }
        }
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = stringResource(R.string.action_networking_delete),
                tint = MaterialTheme.colors.onBackground
            )
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview
@Composable
fun EmailItemPreview() {
    Conferences4HallTheme {
        Scaffold {
            LazyColumn() {
                item {
                    UserItem(
                        user = NetworkingUi.fake.users[0],
                        onClick = {}
                    )
                    Divider(color = MaterialTheme.colors.onBackground)
                }
                item {
                    UserItem(
                        user = NetworkingUi.fake.users[0],
                        onClick = {}
                    )
                    Divider(color = MaterialTheme.colors.onBackground)
                }
                item {
                    UserItem(
                        user = NetworkingUi.fake.users[0],
                        onClick = {}
                    )
                }
            }
        }
    }
}
