package com.paligot.confily.style.events.socials

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.paligot.confily.style.theme.Conferences4HallTheme

@Preview
@Composable
private fun SocialItemPreview() {
    Conferences4HallTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SocialIcons.Twitter(text = "", onClick = {})
            SocialIcons.Mastodon(text = "", onClick = {})
            SocialIcons.GitHub(text = "", onClick = {})
            SocialIcons.LinkedIn(text = "", onClick = {})
            SocialIcons.Website(text = "", onClick = {})
        }
    }
}
