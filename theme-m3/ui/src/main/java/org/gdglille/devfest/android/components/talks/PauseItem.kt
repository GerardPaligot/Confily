package org.gdglille.devfest.android.components.talks

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.R

@Composable
fun PauseItem(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(size = 4.dp),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text(
            text = stringResource(R.string.text_pause_item),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(8.dp),
        )
    }
}

@Preview
@Composable
fun PauseItemPreview() {
    Conferences4HallTheme {
        PauseItem()
    }
}
