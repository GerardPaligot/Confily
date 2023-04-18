package org.gdglille.devfest.android.components.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Navigation
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.android.theme.placeholder
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.models.EventUi

@Composable
fun AddressCard(
    formattedAddress: ImmutableList<String>,
    onItineraryClicked: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    hasGpsLocation: Boolean = true,
    shape: Shape = MaterialTheme.shapes.small,
) {
    Card(
        modifier = modifier.wrapContentHeight(),
        shape = shape,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clearAndSetSemantics {
                        contentDescription = formattedAddress.joinToString(", ")
                    }
            ) {
                formattedAddress.forEachIndexed { index, address ->
                    val weight = if (index == 0) FontWeight.Bold else FontWeight.Normal
                    Text(
                        text = address,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = weight,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.placeholder(visible = isLoading)
                    )
                }
            }
            if (hasGpsLocation) {
                IconButton(onClick = onItineraryClicked) {
                    Icon(
                        imageVector = Icons.Outlined.Navigation,
                        contentDescription = stringResource(R.string.semantic_start_itinerary),
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(32.dp).placeholder(isLoading)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun AddressCardPreview() {
    Conferences4HallTheme {
        AddressCard(
            formattedAddress = EventUi.fake.eventInfo.formattedAddress,
            onItineraryClicked = {}
        )
    }
}
