package org.gdglille.devfest.android.theme.m3.navigation

import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.android.ui.resources.actions.TopAction

object TopActions {
    val share = TopAction(
        id = ActionIds.SHARE_ID,
        icon = R.drawable.ic_mtrl_share_line,
        contentDescription = R.string.action_share_talk
    )
    val filters = TopAction(
        id = ActionIds.FILTERS,
        icon = R.drawable.ic_mtrl_filter_line,
        contentDescription = R.string.action_filtering
    )
    val filtersFilled = TopAction(
        id = ActionIds.FILTERS,
        icon = R.drawable.ic_mtrl_filter_fill,
        contentDescription = R.string.action_filtering
    )
    val disconnect = TopAction(
        id = ActionIds.DISCONNECT,
        icon = R.drawable.ic_mtrl_power_off,
        contentDescription = R.string.action_power_off
    )
    val export = TopAction(
        id = ActionIds.EXPORT,
        icon = R.drawable.ic_mtrl_upgrade_line,
        contentDescription = R.string.action_export
    )
}
