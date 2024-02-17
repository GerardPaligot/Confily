package org.gdglille.devfest.android.theme.m3.partners.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.floor
import org.gdglille.devfest.android.theme.m3.partners.ui.partners.PartnerDivider
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.android.theme.m3.style.Scaffold
import org.gdglille.devfest.android.theme.m3.style.SpacingTokens
import org.gdglille.devfest.android.theme.m3.style.partners.items.PartnerItem
import org.gdglille.devfest.android.theme.m3.style.placeholder
import org.gdglille.devfest.android.theme.m3.style.toDp
import org.gdglille.devfest.models.ui.PartnerGroupsUi

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PartnersGridScreen(
    partners: PartnerGroupsUi,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    isLoading: Boolean = false,
    onPartnerClick: (id: String) -> Unit
) {
    Scaffold(
        title = stringResource(id = R.string.screen_partners),
        modifier = modifier,
        hasScrollBehavior = false
    ) {
        BoxWithConstraints(modifier = Modifier.padding(it)) {
            val minSize = 100.dp
            val mediumSpacing = SpacingTokens.MediumSpacing.toDp()
            val count = floor((this.maxWidth - mediumSpacing * 2) / minSize).toInt()
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = minSize),
                horizontalArrangement = Arrangement.spacedBy(mediumSpacing),
                verticalArrangement = Arrangement.spacedBy(mediumSpacing),
                contentPadding = PaddingValues(
                    vertical = SpacingTokens.LargeSpacing.toDp(),
                    horizontal = mediumSpacing
                ),
                state = state,
            ) {
                partners.groups.forEach {
                    item(span = { GridItemSpan(currentLineSpan = count) }) {
                        PartnerDivider(title = it.type)
                    }
                    items(it.partners) {
                        PartnerItem(
                            url = it.logoUrl,
                            contentDescription = it.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .placeholder(visible = isLoading),
                            onClick = { onPartnerClick(it.id) }
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
private fun PartnersGridScreenPreview() {
    Conferences4HallTheme {
        PartnersGridScreen(
            partners = PartnerGroupsUi.fake,
            onPartnerClick = {}
        )
    }
}
