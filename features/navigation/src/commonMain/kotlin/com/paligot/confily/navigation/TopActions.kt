package com.paligot.confily.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.PowerOff
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Upgrade
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.action_export
import com.paligot.confily.resources.action_filtering
import com.paligot.confily.resources.action_power_off
import com.paligot.confily.resources.action_share_talk
import com.paligot.confily.style.theme.actions.TopAction

object TopActions {
    val share = TopAction(
        id = ActionIds.SHARE_ID,
        icon = Icons.Outlined.Share,
        contentDescription = Resource.string.action_share_talk
    )
    val filters = TopAction(
        id = ActionIds.FILTERS,
        icon = Icons.Outlined.FilterList,
        contentDescription = Resource.string.action_filtering
    )
    val filtersFilled = TopAction(
        id = ActionIds.FILTERS,
        icon = Icons.Filled.FilterList,
        contentDescription = Resource.string.action_filtering
    )
    val disconnect = TopAction(
        id = ActionIds.DISCONNECT,
        icon = Icons.Outlined.PowerOff,
        contentDescription = Resource.string.action_power_off
    )
    val export = TopAction(
        id = ActionIds.EXPORT,
        icon = Icons.Outlined.Upgrade,
        contentDescription = Resource.string.action_export
    )
}
