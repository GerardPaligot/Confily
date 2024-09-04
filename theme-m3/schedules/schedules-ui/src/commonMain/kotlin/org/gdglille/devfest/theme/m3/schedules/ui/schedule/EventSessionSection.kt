package org.gdglille.devfest.theme.m3.schedules.ui.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.text_schedule_minutes
import org.gdglille.devfest.android.theme.m3.style.tags.MediumTag
import org.gdglille.devfest.android.theme.m3.style.tags.TagDefaults
import org.gdglille.devfest.models.ui.EventSessionItemUi
import org.gdglille.devfest.theme.m3.style.schedules.findTimeImageVector
import org.jetbrains.compose.resources.stringResource

@Composable
fun EventSessionSection(
    session: EventSessionItemUi,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    titleTextStyle: TextStyle = MaterialTheme.typography.headlineMedium
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(text = session.title, color = color, style = titleTextStyle)
        Row {
            MediumTag(
                text = session.slotTime,
                icon = Icons.Outlined.Schedule,
                colors = TagDefaults.unStyledColors()
            )
            MediumTag(
                text = session.room,
                icon = Icons.Outlined.Videocam,
                colors = TagDefaults.unStyledColors()
            )
            MediumTag(
                text = stringResource(
                    Resource.string.text_schedule_minutes,
                    session.timeInMinutes.toString()
                ),
                icon = session.timeInMinutes.findTimeImageVector(),
                colors = TagDefaults.unStyledColors()
            )
        }
    }
}
