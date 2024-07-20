package org.gdglille.devfest.android.theme.m3.style.events.socials

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.theme.m3.style.events.socials.SocialsSection

@Preview
@Composable
internal fun SocialsSectionPreview() {
    Conferences4HallTheme {
        SocialsSection(
            title = "Gérard Paligot",
            pronouns = "Him/He",
            subtitle = null,
            onLinkClicked = {},
            twitterUrl = "",
            githubUrl = "",
            linkedinUrl = "",
            websiteUrl = ""
        )
    }
}
