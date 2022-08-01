package org.gdglille.devfest.android.theme.vitamin.ui.components.speakers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import org.gdglille.devfest.android.theme.vitamin.ui.R
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.models.SpeakerUi

@Composable
fun SpeakerHeader(
    url: String,
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit
) {
    Box(modifier = modifier) {
        SpeakerAvatar(
            url = url,
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().height(230.dp)
        )
        IconButton(onClick = onBackClicked) {
            Icon(
                painter = painterResource(R.drawable.ic_vtmn_close_line),
                contentDescription = stringResource(id = R.string.action_back),
                tint = VitaminTheme.colors.vtmnContentPrimaryReversed
            )
        }
    }
}

@Preview
@Composable
fun SpeakerHeaderPreview() {
    Conferences4HallTheme {
        SpeakerHeader(
            url = SpeakerUi.fake.url,
            onBackClicked = {}
        )
    }
}
