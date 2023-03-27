package org.gdglille.devfest.android.components.structure

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.persistentListOf
import org.gdglille.devfest.android.BottomActions
import org.gdglille.devfest.android.TopActions
import org.gdglille.devfest.android.components.appbars.BottomAppBar
import org.gdglille.devfest.android.components.appbars.Tab
import org.gdglille.devfest.android.components.appbars.TopAppBar
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.android.ui.resources.actions.BottomAction
import org.gdglille.devfest.android.ui.resources.actions.FabAction
import org.gdglille.devfest.android.ui.resources.actions.TabAction
import org.gdglille.devfest.android.ui.resources.actions.TopAction
import org.gdglille.devfest.android.ui.resources.models.BottomActionsUi
import org.gdglille.devfest.android.ui.resources.models.TabActionsUi
import org.gdglille.devfest.android.ui.resources.models.TopActionsUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Scaffold(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    topActions: TopActionsUi = TopActionsUi(),
    tabActions: TabActionsUi = TabActionsUi(),
    bottomActions: BottomActionsUi = BottomActionsUi(),
    fabAction: FabAction? = null,
    routeSelected: String? = null,
    tabSelectedIndex: Int? = null,
    onTopActionClicked: (TopAction) -> Unit = {},
    onTabClicked: (TabAction) -> Unit = {},
    onBottomActionClicked: (BottomAction) -> Unit = {},
    onFabActionClicked: (FabAction) -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Column {
                TopAppBar(
                    title = stringResource(title),
                    topActionsUi = topActions,
                    onActionClicked = onTopActionClicked
                )
                if (tabActions.actions.count() > 1 && tabSelectedIndex != null) {
                    ScrollableTabRow(
                        selectedTabIndex = tabSelectedIndex,
                        divider = {},
                        indicator = {}
                    ) {
                        tabActions.actions.forEachIndexed { index, tabAction ->
                            tabAction.label?.let {
                                Tab(
                                    selected = index == tabSelectedIndex,
                                    onClick = { onTabClicked(tabAction) },
                                    text = it
                                )
                            } ?: kotlin.run {
                                Tab(
                                    selected = index == tabSelectedIndex,
                                    onClick = { onTabClicked(tabAction) },
                                    text = stringResource(tabAction.labelId)
                                )
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            if (bottomActions.actions.isNotEmpty() && routeSelected != null) {
                BottomAppBar(
                    bottomActions = bottomActions,
                    routeSelected = routeSelected,
                    onClick = onBottomActionClicked
                )
            }
        },
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
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                        onFabActionClicked(fabAction)
                    }
                )
            }
        },
        content = content
    )
}

@Preview
@Composable
internal fun ScaffoldPreview() {
    Conferences4HallTheme {
        Scaffold(
            title = R.string.screen_agenda,
            topActions = TopActionsUi(
                actions = persistentListOf(
                    TopActions.share
                )
            ),
            tabActions = TabActionsUi(
                actions = persistentListOf(
                    TabAction(route = "day-1", 0, "11 Oct"),
                    TabAction(route = "day-2", 0, "12 Oct"),
                )
            ),
            bottomActions = BottomActionsUi(
                persistentListOf(
                    BottomActions.agenda,
                    BottomActions.partners,
                    BottomActions.info
                )
            ),
            routeSelected = "agenda",
            tabSelectedIndex = 0,
            content = {}
        )
    }
}
