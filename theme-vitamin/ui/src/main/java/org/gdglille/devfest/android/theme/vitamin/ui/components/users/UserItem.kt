package org.gdglille.devfest.android.theme.vitamin.ui.components.users

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decathlon.vitamin.compose.dividers.VitaminDividers
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import org.gdglille.devfest.android.theme.vitamin.ui.components.buttons.IconButtonTertiary
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.models.NetworkingUi
import org.gdglille.devfest.models.UserNetworkingUi
import com.decathlon.vitamin.compose.foundation.R as RVitamin

@Composable
fun UserItem(
    user: UserNetworkingUi,
    modifier: Modifier = Modifier,
    nameColor: Color = VitaminTheme.colors.vtmnContentPrimary,
    metaColor: Color = VitaminTheme.colors.vtmnContentSecondary,
    nameStyle: TextStyle = VitaminTheme.typography.body2,
    metaStyle: TextStyle = VitaminTheme.typography.body3,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.weight(1f)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = user.firstName,
                    color = nameColor,
                    style = nameStyle
                )
                Text(
                    text = user.lastName,
                    color = nameColor,
                    style = nameStyle
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(RVitamin.drawable.ic_vtmn_mail_line),
                    contentDescription = null,
                    tint = metaColor,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = user.email,
                    color = metaColor,
                    style = metaStyle,
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(RVitamin.drawable.ic_vtmn_suitcase_line),
                    contentDescription = null,
                    tint = metaColor,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = user.company,
                    color = metaColor,
                    style = metaStyle
                )
            }
        }
        IconButtonTertiary(
            painter = painterResource(id = RVitamin.drawable.ic_vtmn_delete_bin_line),
            contentDescription = stringResource(R.string.action_networking_delete),
            onClick = onClick
        )
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
                        user = NetworkingUi.fake.users[0]
                    ) {}
                    VitaminDividers.Middle()
                }
                item {
                    UserItem(
                        user = NetworkingUi.fake.users[0]
                    ) {}
                    VitaminDividers.Middle()
                }
                item {
                    UserItem(
                        user = NetworkingUi.fake.users[0]
                    ) {}
                }
            }
        }
    }
}
