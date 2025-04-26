package com.paligot.confily.partners.panes

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
import com.paligot.confily.partners.semantics.PartnersSemantics
import com.paligot.confily.partners.ui.PartnerDivider
import com.paligot.confily.partners.ui.models.PartnerGroupsUi
import com.paligot.confily.style.components.placeholder.placeholder
import com.paligot.confily.style.partners.items.PartnerItem
import com.paligot.confily.style.theme.ConfilyTheme
import com.paligot.confily.style.theme.SpacingTokens
import com.paligot.confily.style.theme.toDp
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.floor

const val NbHorizontalPadding = 2

@Composable
internal fun PartnersGridContent(
    partners: PartnerGroupsUi,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    isLoading: Boolean = false,
    onPartnerClick: (id: String) -> Unit
) {
    BoxWithConstraints(modifier = modifier) {
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

@Preview
@Composable
private fun PartnersGridContentPreview() {
    ConfilyTheme {
        PartnersGridContent(
            partners = PartnerGroupsUi.fake,
            onPartnerClick = {}
        )
    }
}
