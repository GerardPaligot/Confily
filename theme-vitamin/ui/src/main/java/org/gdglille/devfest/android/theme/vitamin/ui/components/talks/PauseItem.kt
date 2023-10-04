package org.gdglille.devfest.android.theme.vitamin.ui.components.talks

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme

@Composable
fun PauseItem(
    title: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(size = 4.dp),
        color = VitaminTheme.colors.vtmnBackgroundTertiary
    ) {
        Text(
            text = title,
            style = VitaminTheme.typography.body3,
            color = VitaminTheme.colors.vtmnContentPrimary,
            modifier = Modifier.padding(8.dp),
        )
    }
}

@Preview
@Composable
fun PauseItemPreview() {
    Conferences4HallTheme {
        PauseItem(title = "Pause")
    }
}
