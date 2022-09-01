package org.gdglille.devfest.android.theme.vitamin.ui.components.talks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.android.theme.vitamin.ui.theme.placeholder

@Composable
fun Time(
    time: String,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Text(
            text = time,
            style = VitaminTheme.typography.h6,
            color = VitaminTheme.colors.vtmnContentPrimary,
            modifier = Modifier
                .padding(top = 4.dp)
                .placeholder(visible = isLoading)
        )
    }
}

@Preview
@Composable
fun TimePreview() {
    Conferences4HallTheme {
        Time(time = "10:00")
    }
}
