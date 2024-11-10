package com.paligot.confily.partners.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.paligot.confily.models.ui.ActivityUi
import com.paligot.confily.style.partners.activities.MediumActivityItem
import com.paligot.confily.style.schedules.time.Time
import com.paligot.confily.style.theme.ConfilyTheme
import com.paligot.confily.style.theme.SpacingTokens
import com.paligot.confily.style.theme.toDp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ActivityItem(
    slotTime: String,
    activities: ImmutableList<ActivityUi>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.SmallSpacing.toDp())
    ) {
        Time(time = slotTime)
        activities.forEach { activityUi ->
            MediumActivityItem(
                activityName = activityUi.activityName,
                partnerName = activityUi.partnerName,
                time = activityUi.startTime
            )
        }
    }
}

@Preview
@Composable
private fun ActivityItemPreview() {
    ConfilyTheme {
        ActivityItem(
            slotTime = "10:00",
            activities = persistentListOf(
                ActivityUi(
                    activityName = "Kahoot",
                    partnerName = "WeLoveDevs",
                    startTime = "10h10:00"
                ),
                ActivityUi(
                    activityName = "Kahoot",
                    partnerName = "WeLoveDevs",
                    startTime = "10:15"
                )
            )
        )
    }
}
