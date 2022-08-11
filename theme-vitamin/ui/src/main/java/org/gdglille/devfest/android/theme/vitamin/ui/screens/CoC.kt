package org.gdglille.devfest.android.theme.vitamin.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.decathlon.vitamin.compose.buttons.VitaminButtons
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import com.halilibo.richtext.ui.RichTextThemeIntegration
import org.gdglille.devfest.android.theme.vitamin.ui.R

@Composable
fun CoC(
    coc: String,
    modifier: Modifier = Modifier,
    onReportClicked: () -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 24.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.screen_coc),
                style = VitaminTheme.typography.h4,
                color = VitaminTheme.colors.vtmnContentPrimary
            )
        }
        item {
            RichTextThemeIntegration(
                textStyle = { VitaminTheme.typography.body3 },
                ProvideTextStyle = null,
                contentColor = { VitaminTheme.colors.vtmnContentPrimary },
                ProvideContentColor = null,
            ) {
                RichText {
                    Markdown(coc)
                }
            }
        }
        item {
            VitaminButtons.Primary(
                text = stringResource(R.string.action_contact_organizers),
                onClick = onReportClicked,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
