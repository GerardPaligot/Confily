package org.gdglille.devfest.android.theme.m3.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.PowerOff
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Upgrade
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.android.theme.m3.style.actions.TopAction

object TopActions {
    val share = TopAction(
        id = ActionIds.SHARE_ID,
        icon = Icons.Outlined.Share,
        contentDescription = R.string.action_share_talk
    )
    val filters = TopAction(
        id = ActionIds.FILTERS,
        icon = Icons.Outlined.FilterList,
        contentDescription = R.string.action_filtering
    )
    val filtersFilled = TopAction(
        id = ActionIds.FILTERS,
        icon = Icons.Filled.FilterList,
        contentDescription = R.string.action_filtering
    )
    val disconnect = TopAction(
        id = ActionIds.DISCONNECT,
        icon = Icons.Outlined.PowerOff,
        contentDescription = R.string.action_power_off
    )
    val export = TopAction(
        id = ActionIds.EXPORT,
        icon = Icons.Outlined.Upgrade,
        contentDescription = R.string.action_export
    )
}
