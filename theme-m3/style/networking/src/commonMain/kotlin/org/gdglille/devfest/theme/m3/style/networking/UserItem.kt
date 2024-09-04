package org.gdglille.devfest.theme.m3.style.networking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BusinessCenter
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.action_networking_delete
import com.paligot.confily.resources.semantic_user_item_company
import com.paligot.confily.resources.semantic_user_item_email
import org.gdglille.devfest.android.theme.m3.style.buttons.IconButton
import org.gdglille.devfest.android.theme.m3.style.toDp
import org.jetbrains.compose.resources.stringResource

@Composable
fun UserItem(
    displayName: String,
    email: String,
    company: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = UserItemDefaults.contentColor,
    nameStyle: TextStyle = UserItemDefaults.nameTextStyle,
    metaStyle: TextStyle = UserItemDefaults.metaTextStyle,
    contentPadding: PaddingValues = UserItemDefaults.contentPadding
) {
    val emailDescription = stringResource(Resource.string.semantic_user_item_email, email)
    val companyDescription = company
        ?.let { stringResource(Resource.string.semantic_user_item_company, it) }
        ?: ""
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(UserItemTokens.ColumnSpacing.toDp()),
            modifier = Modifier
                .weight(1f)
                .clearAndSetSemantics {
                    contentDescription = "$displayName $emailDescription $companyDescription"
                }
        ) {
            Text(text = displayName, color = color, style = nameStyle)
            Row(
                horizontalArrangement = Arrangement.spacedBy(UserItemTokens.IconTextSpacing.toDp()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(UserItemDefaults.iconSize)
                )
                Text(text = email, color = color.copy(alpha = .73f), style = metaStyle)
            }
            if (company != null && company != "") {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(UserItemTokens.IconTextSpacing.toDp()),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.BusinessCenter,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(UserItemDefaults.iconSize)
                    )
                    Text(text = company, color = color.copy(alpha = .73f), style = metaStyle)
                }
            }
        }
        IconButton(
            imageVector = Icons.Outlined.Delete,
            contentDescription = stringResource(Resource.string.action_networking_delete),
            onClick = onClick
        )
    }
}
