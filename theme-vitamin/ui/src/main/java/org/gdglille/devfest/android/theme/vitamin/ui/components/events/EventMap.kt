package org.gdglille.devfest.android.theme.vitamin.ui.components.events

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import kotlinx.collections.immutable.ImmutableList
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.android.theme.vitamin.ui.theme.placeholder
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.models.EventUi
import com.decathlon.vitamin.compose.foundation.R as RVitamin

@Composable
fun EventMap(
    formattedAddress: ImmutableList<String>,
    onItineraryClicked: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    shape: Shape = RoundedCornerShape(4.dp),
) {
    Card(
        modifier = modifier.wrapContentHeight(),
        shape = shape,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                formattedAddress.forEachIndexed { index, address ->
                    val color = if (index == 0) {
                        VitaminTheme.colors.vtmnContentPrimary
                    } else {
                        VitaminTheme.colors.vtmnContentSecondary
                    }
                    val weight = if (index == 0) FontWeight.Bold else FontWeight.Normal
                    Text(
                        text = address,
                        style = VitaminTheme.typography.body2,
                        fontWeight = weight,
                        color = color,
                        modifier = Modifier.placeholder(visible = isLoading)
                    )
                }
            }
            IconButton(onClick = onItineraryClicked) {
                Icon(
                    painter = painterResource(RVitamin.drawable.ic_vtmn_direction_line),
                    contentDescription = stringResource(R.string.semantic_start_itinerary),
                    tint = VitaminTheme.colors.vtmnContentAction,
                    modifier = Modifier.size(32.dp).placeholder(isLoading)
                )
            }
        }
    }
}

@Preview
@Composable
fun EventMapPreview() {
    Conferences4HallTheme {
        EventMap(
            formattedAddress = EventUi.fake.eventInfo.formattedAddress,
            onItineraryClicked = {}
        )
    }
}
