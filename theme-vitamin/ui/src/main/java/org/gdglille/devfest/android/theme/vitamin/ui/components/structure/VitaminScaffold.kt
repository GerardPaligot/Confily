package org.gdglille.devfest.android.theme.vitamin.ui.components.structure

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.decathlon.vitamin.compose.appbars.bottomnavigations.SelectedActionItem
import com.decathlon.vitamin.compose.appbars.bottomnavigations.VitaminBottomNavigations
import com.decathlon.vitamin.compose.appbars.topbars.ActionItem
import com.decathlon.vitamin.compose.appbars.topbars.VitaminTopBars
import com.decathlon.vitamin.compose.appbars.topbars.icons.VitaminNavigationIconButtons
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import com.decathlon.vitamin.compose.tabs.TabItem
import com.decathlon.vitamin.compose.tabs.VitaminTabs
import org.gdglille.devfest.android.theme.vitamin.ui.BottomActions
import org.gdglille.devfest.android.theme.vitamin.ui.FabActions
import org.gdglille.devfest.android.theme.vitamin.ui.R
import org.gdglille.devfest.android.theme.vitamin.ui.TabActions
import org.gdglille.devfest.android.theme.vitamin.ui.TopActions
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.BottomAction
import org.gdglille.devfest.android.ui.resources.FabAction
import org.gdglille.devfest.android.ui.resources.TabAction
import org.gdglille.devfest.android.ui.resources.TopAction

@Composable
fun VitaminScaffold(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    topActions: List<TopAction> = emptyList(),
    tabActions: List<TabAction> = emptyList(),
    bottomActions: List<BottomAction> = emptyList(),
    fabAction: FabAction? = null,
    scrollable: Boolean = false,
    routeSelected: String? = null,
    onTopActionClicked: (TopAction) -> Unit = {},
    onTabClicked: (TabAction) -> Unit = {},
    onBottomActionClicked: (BottomAction) -> Unit = {},
    onFabActionClicked: (FabAction) -> Unit = {},
    navigationIcon: @Composable (VitaminNavigationIconButtons.() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    val context = LocalContext.current
    Scaffold(
        modifier = modifier,
        topBar = {
            Column {
                VitaminTopBars.Primary(
                    title = stringResource(title),
                    navigationIcon = navigationIcon,
                    actions = topActions.map {
                        ActionItem(
                            icon = painterResource(it.icon),
                            contentDescription = it.contentDescription?.let { stringResource(it) },
                            onClick = {
                                onTopActionClicked(it)
                            }
                        )
                    }
                )
                if (tabActions.isNotEmpty()) {
                    val tabItems = tabActions.map {
                        TabItem(label = stringResource(it.label), selected = it.route == routeSelected)
                    }
                    if (scrollable) {
                        VitaminTabs.Scrollable(
                            tabItems = tabItems,
                            onTabClicked = { item ->
                                tabActions.find { item.label == context.getString(it.label) }?.let(onTabClicked)
                            }
                        )
                    } else {
                        VitaminTabs.Fixed(
                            tabItems = tabItems,
                            onTabClicked = { item ->
                                tabActions.find { item.label == context.getString(it.label) }?.let(onTabClicked)
                            }
                        )
                    }
                }
            }
        },
        bottomBar = {
            if (bottomActions.isNotEmpty() && routeSelected != null) {
                VitaminBottomNavigations.Primary(
                    actions = bottomActions.map { action ->
                        val hasSelectedRoutes = action.selectedRoutes.find { it == routeSelected } != null
                        val selected = hasSelectedRoutes || action.route == routeSelected
                        SelectedActionItem(
                            icon = painterResource(if (selected) action.iconSelected else action.icon),
                            contentDescription = action.contentDescription?.let { stringResource(it) },
                            content = { Text(text = stringResource(action.label)) },
                            selected = selected,
                            onClick = {
                                onBottomActionClicked(action)
                                return@SelectedActionItem true
                            }
                        )
                    }
                )
            }
        },
        backgroundColor = VitaminTheme.colors.vtmnBackgroundSecondary,
        contentColor = VitaminTheme.colors.vtmnContentPrimary,
        content = content,
        floatingActionButton = {
            if (fabAction != null) {
                ExtendedFloatingActionButton(
                    text = { Text(text = stringResource(fabAction.label)) },
                    icon = {
                        Icon(
                            painter = painterResource(fabAction.icon),
                            contentDescription = fabAction.contentDescription?.let { stringResource(it) }
                        )
                    },
                    backgroundColor = VitaminTheme.colors.vtmnBackgroundBrandPrimary,
                    contentColor = VitaminTheme.colors.vtmnContentPrimaryReversed,
                    onClick = {
                        onFabActionClicked(fabAction)
                    }
                )
            }
        },
    )
}

@Preview
@Composable
internal fun VitaminScaffoldPreview() {
    Conferences4HallTheme {
        VitaminScaffold(
            title = R.string.screen_agenda,
            topActions = listOf(TopActions.share),
            tabActions = listOf(
                TabActions.event,
                TabActions.menus,
                TabActions.qanda,
                TabActions.coc
            ),
            bottomActions = listOf(
                BottomActions.agenda,
                BottomActions.speakers,
                BottomActions.info
            ),
            fabAction = FabActions.report,
            scrollable = true,
            routeSelected = "agenda",
            content = {}
        )
    }
}
