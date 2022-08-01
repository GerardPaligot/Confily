package org.gdglille.devfest.android.theme.vitamin.ui.components.partners

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decathlon.vitamin.compose.dividers.VitaminDividers
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme

@Composable
fun PartnerDivider(
    title: String,
    modifier: Modifier = Modifier,
    titleColor: Color = VitaminTheme.colors.vtmnContentSecondary,
    style: TextStyle = VitaminTheme.typography.subtitle1
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            VitaminDividers.FullBleed(modifier = Modifier.weight(1f))
            Text(text = title, style = style, color = titleColor)
            VitaminDividers.FullBleed(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview
@Composable
fun PartnerDividerPreview() {
    Conferences4HallTheme {
        PartnerDivider(title = "Gold")
    }
}
