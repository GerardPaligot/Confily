package org.gdglille.devfest.android.components.users

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BusinessCenter
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.components.buttons.IconButton
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.models.NetworkingUi
import org.gdglille.devfest.models.UserNetworkingUi

@Composable
fun UserItem(
    user: UserNetworkingUi,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    nameStyle: TextStyle = MaterialTheme.typography.titleMedium,
    metaStyle: TextStyle = MaterialTheme.typography.bodySmall,
    contentPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
    onClick: () -> Unit
) {
    val email = stringResource(id = R.string.semantic_user_item_email, user.email)
    val work = if (user.company == "") "" else {
        stringResource(id = R.string.semantic_user_item_company, user.company)
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .weight(1f)
                .clearAndSetSemantics {
                    contentDescription = "${user.firstName} ${user.lastName} $email $work"
                }
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
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = user.email,
                    color = color.copy(alpha = .73f),
                    style = metaStyle
                )
            }
            if (user.company != "") {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.BusinessCenter,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = user.company,
                        color = color.copy(alpha = .73f),
                        style = metaStyle
                    )
                }
            }
        }
        IconButton(
            imageVector = Icons.Outlined.Delete,
            contentDescription = stringResource(R.string.action_networking_delete),
            onClick = onClick
        )
    }
}

@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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
                    Divider(color = MaterialTheme.colorScheme.onBackground)
                }
                item {
                    UserItem(
                        user = NetworkingUi.fake.users[0],
                        onClick = {}
                    )
                    Divider(color = MaterialTheme.colorScheme.onBackground)
                }
                item {
                    UserItem(
                        user = NetworkingUi.fake.users[0].copy(company = ""),
                        onClick = {}
                    )
                }
            }
        }
    }
}
