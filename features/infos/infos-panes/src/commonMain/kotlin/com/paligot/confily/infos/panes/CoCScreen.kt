package com.paligot.confily.infos.panes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import com.paligot.confily.models.ui.CoCUi
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.action_contact_organizers_mail
import com.paligot.confily.resources.action_contact_organizers_phone
import com.paligot.confily.resources.screen_coc
import com.paligot.confily.style.components.markdown.MarkdownText
import org.jetbrains.compose.resources.stringResource

@Composable
fun CoCScreen(
    coc: CoCUi,
    onReportByPhoneClicked: (String) -> Unit,
    onReportByEmailClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 24.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = stringResource(Resource.string.screen_coc),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        item {
            MarkdownText(
                text = coc.text,
                modifier = Modifier.clearAndSetSemantics {
                    contentDescription = coc.text
                }
            )
        }
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (coc.phone != null) {
                    Button(onClick = { onReportByPhoneClicked(coc.phone!!) }) {
                        Text(text = stringResource(Resource.string.action_contact_organizers_phone))
                    }
                }
                if (coc.email != null) {
                    Button(onClick = { onReportByEmailClicked(coc.email!!) }) {
                        Text(text = stringResource(Resource.string.action_contact_organizers_mail))
                    }
                }
            }
        }
    }
}
