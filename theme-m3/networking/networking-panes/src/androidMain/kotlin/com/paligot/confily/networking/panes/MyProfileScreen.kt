package com.paligot.confily.networking.panes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.paligot.confily.models.ui.UserProfileUi
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.action_edit_profile
import com.paligot.confily.resources.semantic_profile_qrcode
import com.paligot.confily.resources.semantic_user_item_company
import com.paligot.confily.resources.semantic_user_item_email
import org.jetbrains.compose.resources.stringResource

@Composable
fun MyProfileScreen(
    profileUi: UserProfileUi,
    onEditInformation: () -> Unit,
    modifier: Modifier = Modifier
) {
    val typography = MaterialTheme.typography.bodyMedium
    val color = MaterialTheme.colorScheme.onBackground
    val email = stringResource(Resource.string.semantic_user_item_email, profileUi.email)
    val work = if (profileUi.company == "") {
        ""
    } else {
        stringResource(Resource.string.semantic_user_item_company, profileUi.company)
    }
    val isInPreview = LocalInspectionMode.current
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 24.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!isInPreview && profileUi.qrCode != null) {
            item {
                Image(
                    bitmap = profileUi.qrCode!!.asImageBitmap(),
                    contentDescription = stringResource(Resource.string.semantic_profile_qrcode),
                    modifier = Modifier.size(196.dp)
                )
            }
        }
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clearAndSetSemantics {
                    contentDescription = "${profileUi.firstName} ${profileUi.lastName} $email $work"
                }
            ) {
                Text(
                    text = "${profileUi.firstName} ${profileUi.lastName}",
                    style = typography,
                    fontWeight = FontWeight.W700,
                    color = color
                )
                if (profileUi.company != "") {
                    Text(
                        text = profileUi.company,
                        style = typography,
                        color = color
                    )
                }
                Text(
                    text = profileUi.email,
                    style = typography,
                    color = color
                )
            }
        }
        item {
            Button(onClick = onEditInformation) {
                Text(text = stringResource(Resource.string.action_edit_profile))
            }
        }
    }
}
