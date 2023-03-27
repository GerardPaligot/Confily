package org.gdglille.devfest.android.theme.vitamin.ui.components.structure

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.decathlon.vitamin.compose.appbars.topbars.ActionItem
import com.decathlon.vitamin.compose.appbars.topbars.VitaminTopBars
import com.decathlon.vitamin.compose.appbars.topbars.icons.VitaminNavigationIconButtons
import com.decathlon.vitamin.compose.tabs.TabItem
import com.decathlon.vitamin.compose.tabs.VitaminTabs
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.android.ui.resources.actions.TabAction
import org.gdglille.devfest.android.ui.resources.actions.TopAction
import org.gdglille.devfest.android.ui.resources.models.TabActionsUi
import org.gdglille.devfest.android.ui.resources.models.TopActionsUi

@Composable
internal fun VitaminTopBar(
    title: Int,
    navigationIcon: @Composable (VitaminNavigationIconButtons.() -> Unit)?,
    topActionsUi: TopActionsUi,
    onTopActionClicked: (TopAction) -> Unit,
    tabActionsUi: TabActionsUi,
    tabSelectedIndex: Int?,
    context: Context,
    onTabClicked: (TabAction) -> Unit
) {
    val expandedMenu = remember { mutableStateOf(false) }
    Column {
        VitaminTopBars.Primary(
            title = stringResource(title),
            navigationIcon = navigationIcon,
            maxActions = topActionsUi.maxActions,
            actions = topActionsUi.actions.map {
                ActionItem(
                    icon = painterResource(it.icon),
                    contentDescription = it.contentDescription?.let { stringResource(it) },
                    onClick = {
                        onTopActionClicked(it)
                    },
                    content = {
                        it.contentDescription?.let { Text(text = stringResource(it)) }
                    }
                )
            },
            expandedMenu = expandedMenu,
            overflowIcon = {
                More(
                    onClick = { expandedMenu.value = !expandedMenu.value },
                    contentDescription = stringResource(id = R.string.action_expanded_menu)
                )
            }
        )
        if (tabActionsUi.actions.size > 1) {
            val tabItems = tabActionsUi.actions.mapIndexed { index, it ->
                if (it.label != null) {
                    TabItem(label = it.label!!, selected = index == tabSelectedIndex)
                } else {
                    TabItem(
                        label = stringResource(it.labelId),
                        selected = index == tabSelectedIndex
                    )
                }
            }
            if (tabActionsUi.scrollable) {
                VitaminTabs.Scrollable(
                    tabItems = tabItems,
                    onTabClicked = { item ->
                        tabActionsUi.actions.find { item.label == context.getString(it.labelId) }
                            ?.let(onTabClicked)
                    }
                )
            } else {
                VitaminTabs.Fixed(
                    tabItems = tabItems,
                    onTabClicked = { item ->
                        tabActionsUi.actions.find { item.label == context.getString(it.labelId) }
                            ?.let(onTabClicked)
                    }
                )
            }
        }
    }
}
