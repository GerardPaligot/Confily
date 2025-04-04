package com.paligot.confily.style.events.socials

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.paligot.confily.style.theme.ConfilyTheme

@Preview
@Composable
private fun SocialsSectionPreview() {
    ConfilyTheme {
        SocialsSection(
            title = "Gérard Paligot",
            pronouns = "Him/He",
            subtitle = null,
            onLinkClicked = {},
            xUrl = "",
            githubUrl = "",
            linkedinUrl = "",
            websiteUrl = ""
        )
    }
}
