package org.gdglille.devfest.android.theme.vitamin.ui.components.tags

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.decathlon.vitamin.compose.foundation.VitaminTheme

@Composable
fun UnStyledTag(
    text: String,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    style: TextStyle = VitaminTheme.typography.body2
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = VitaminTheme.colors.vtmnContentPrimary,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = text,
            color = VitaminTheme.colors.vtmnContentPrimary,
            style = style
        )
    }
}
