package org.gdglille.devfest.android.theme.vitamin.ui.screens.networking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.R

@Composable
fun EmptyContacts(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp, horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.text_empty_contacts),
            style = VitaminTheme.typography.body2,
            color = VitaminTheme.colors.vtmnContentPrimary,
            modifier = modifier
        )
    }
}

@Preview
@Composable
fun EmptyContactsPreview() {
    Conferences4HallTheme {
        EmptyContacts()
    }
}
