package com.paligot.confily.partners.panes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.paligot.confily.models.ui.ActivityUi
import com.paligot.confily.partners.ui.ActivityItem
import com.paligot.confily.style.theme.ConfilyTheme
import com.paligot.confily.style.theme.SpacingTokens
import com.paligot.confily.style.theme.toDp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun PartnersActivitiesContent(
    activities: ImmutableMap<String, ImmutableList<ActivityUi>>,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = modifier,
        state = state,
        contentPadding = PaddingValues(
            vertical = SpacingTokens.LargeSpacing.toDp(),
            horizontal = SpacingTokens.MediumSpacing.toDp()
        ),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.MediumSpacing.toDp())
    ) {
        items(activities.entries.toList()) { entry ->
            ActivityItem(
                slotTime = entry.key,
                activities = entry.value
            )
        }
    }
}

@Preview
@Composable
private fun PartnersActivitiesContentPreview() {
    ConfilyTheme {
        Scaffold {
            PartnersActivitiesContent(
                activities = persistentMapOf(
                    "10h00" to persistentListOf(
                        ActivityUi.fake,
                        ActivityUi.fake
                    ),
                    "11h00" to persistentListOf(
                        ActivityUi.fake,
                        ActivityUi.fake
                    )
                )
            )
        }
    }
}
