package org.gdglille.devfest.theme.m3.partners.screens

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.paligot.confily.models.ui.PartnerGroupsUi
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.screen_partners
import com.paligot.confily.style.partners.items.PartnerItem
import com.paligot.confily.style.theme.Scaffold
import com.paligot.confily.style.theme.SpacingTokens
import com.paligot.confily.style.theme.toDp
import org.gdglille.devfest.android.theme.m3.partners.semantics.PartnersSemantics
import org.gdglille.devfest.theme.m3.partners.ui.PartnerDivider
import org.gdglille.devfest.theme.m3.style.placeholder.placeholder
import org.jetbrains.compose.resources.stringResource
import kotlin.math.floor

const val NbHorizontalPadding = 2

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
        title = stringResource(Resource.string.screen_partners),
        modifier = modifier,
        hasScrollBehavior = false
    ) {
        BoxWithConstraints(modifier = Modifier.padding(top = it.calculateTopPadding())) {
            val minSize = 100.dp
            val mediumSpacing = SpacingTokens.MediumSpacing.toDp()
            val widthSize = this.maxWidth - (mediumSpacing * NbHorizontalPadding)
            val count = floor(widthSize / minSize).toInt()
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = minSize),
                modifier = Modifier.testTag(PartnersSemantics.list),
                verticalArrangement = Arrangement.spacedBy(mediumSpacing),
                contentPadding = PaddingValues(
                    vertical = SpacingTokens.LargeSpacing.toDp(),
                    horizontal = mediumSpacing
                ),
                state = state
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
                                .padding(horizontal = SpacingTokens.SmallSpacing.toDp())
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
