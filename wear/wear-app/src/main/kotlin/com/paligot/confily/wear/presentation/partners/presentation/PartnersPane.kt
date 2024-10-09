package com.paligot.confily.wear.presentation.partners.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.ScrollIndicator
import androidx.wear.compose.material3.Text
import coil3.compose.rememberAsyncImagePainter
import com.paligot.confily.wear.theme.buttons.ExtendedActionButton
import com.paligot.confily.wear.theme.buttons.IconActionButton
import kotlinx.collections.immutable.ImmutableList

@Composable
fun PartnersPane(
    title: String,
    partners: ImmutableList<PartnerModelUi>,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    val scrollState = rememberScalingLazyListState()
    ScreenScaffold(
        scrollState = scrollState,
        modifier = modifier,
        scrollIndicator = {
            ScrollIndicator(scrollState, modifier = Modifier.align(Alignment.CenterEnd))
        }
    ) {
        ScalingLazyColumn(
            contentPadding = PaddingValues(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            item { ListHeader { Text(title) } }
            items(partners) { partner ->
                ExtendedActionButton(
                    image = {
                        IconActionButton(
                            painter = rememberAsyncImagePainter(model = partner.url),
                            background = Color.White
                        )
                    },
                    label = {
                        Text(
                            text = partner.name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    onClick = { onClick(partner.id) }
                )
            }
        }
    }
}
