package org.gdglille.devfest.android.theme.m3.style.tags

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Suppress("LongMethod", "UnusedPrivateMember")
@Preview
@Composable
private fun MediumTagPreview() {
    Conferences4HallTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(4.dp)
        ) {
            MediumTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid
            )
            MediumTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid,
                colors = TagDefaults.brickColors()
            )
            MediumTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid,
                colors = TagDefaults.cobaltColors()
            )
            MediumTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid,
                colors = TagDefaults.emeraldColors()
            )
            MediumTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid,
                colors = TagDefaults.goldColors()
            )
            MediumTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid,
                colors = TagDefaults.gravelColors()
            )
            MediumTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid,
                colors = TagDefaults.jadeColors()
            )
            MediumTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid,
                colors = TagDefaults.saffronColors()
            )
        }
    }
}

@Suppress("LongMethod", "UnusedPrivateMember")
@Preview
@Composable
private fun SmallTagPreview() {
    Conferences4HallTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(4.dp)
        ) {
            SmallTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid
            )
            SmallTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid,
                colors = TagDefaults.brickColors()
            )
            SmallTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid,
                colors = TagDefaults.cobaltColors()
            )
            SmallTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid,
                colors = TagDefaults.emeraldColors()
            )
            SmallTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid,
                colors = TagDefaults.goldColors()
            )
            SmallTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid,
                colors = TagDefaults.gravelColors()
            )
            SmallTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid,
                colors = TagDefaults.jadeColors()
            )
            SmallTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid,
                colors = TagDefaults.saffronColors()
            )
        }
    }
}
