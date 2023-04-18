package org.gdglille.devfest.android.components.partners

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.components.structure.SocialsSection
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.models.PartnerItemUi

@ExperimentalMaterial3Api
@Composable
fun PartnerDetailSection(
    partnerItemUi: PartnerItemUi,
    onLinkClicked: (url: String) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Conferences4HallTheme(useDarkTheme = false) {
            PartnerItem(
                partnerUi = partnerItemUi,
                isLoading = isLoading,
                modifier = Modifier.size(128.dp).clearAndSetSemantics { },
                onClick = {}
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        SocialsSection(
            title = partnerItemUi.name,
            pronouns = null,
            subtitle = null,
            detailed = partnerItemUi.description,
            isLoading = isLoading,
            twitterUrl = partnerItemUi.twitterUrl,
            githubUrl = null,
            linkedinUrl = partnerItemUi.linkedinUrl,
            websiteUrl = partnerItemUi.siteUrl,
            onLinkClicked = onLinkClicked
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PartnerDetailSectionPreview() {
    Conferences4HallTheme {
        PartnerDetailSection(
            partnerItemUi = PartnerItemUi.fake,
            onLinkClicked = {}
        )
    }
}
