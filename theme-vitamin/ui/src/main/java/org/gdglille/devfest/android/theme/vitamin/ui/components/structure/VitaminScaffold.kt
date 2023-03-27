package org.gdglille.devfest.android.theme.vitamin.ui.components.structure

import androidx.annotation.StringRes
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
import com.decathlon.vitamin.compose.appbars.topbars.icons.VitaminNavigationIconButtons
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import kotlinx.collections.immutable.persistentListOf
import org.gdglille.devfest.android.theme.vitamin.ui.BottomActions
import org.gdglille.devfest.android.theme.vitamin.ui.FabActions
import org.gdglille.devfest.android.theme.vitamin.ui.TabActions
import org.gdglille.devfest.android.theme.vitamin.ui.TopActions
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.android.ui.resources.actions.BottomAction
import org.gdglille.devfest.android.ui.resources.actions.FabAction
import org.gdglille.devfest.android.ui.resources.actions.TabAction
import org.gdglille.devfest.android.ui.resources.actions.TopAction
import org.gdglille.devfest.android.ui.resources.models.BottomActionsUi
import org.gdglille.devfest.android.ui.resources.models.TabActionsUi
import org.gdglille.devfest.android.ui.resources.models.TopActionsUi

@Composable
fun VitaminScaffold(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    topActionsUi: TopActionsUi = TopActionsUi(),
    tabActionsUi: TabActionsUi = TabActionsUi(),
    bottomActionsUi: BottomActionsUi = BottomActionsUi(),
    fabAction: FabAction? = null,
    routeSelected: String? = null,
    tabSelectedIndex: Int? = null,
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
            VitaminTopBar(
                title = title,
                navigationIcon = navigationIcon,
                topActionsUi = topActionsUi,
                onTopActionClicked = onTopActionClicked,
                tabActionsUi = tabActionsUi,
                tabSelectedIndex = tabSelectedIndex,
                context = context,
                onTabClicked = onTabClicked
            )
        },
        bottomBar = {
            if (bottomActionsUi.actions.isNotEmpty() && routeSelected != null) {
                VitaminBottomNavigations.Primary(
                    actions = bottomActionsUi.actions.map { action ->
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
            topActionsUi = TopActionsUi(
                actions = persistentListOf(TopActions.share)
            ),
            tabActionsUi = TabActionsUi(
                scrollable = false,
                actions = persistentListOf(
                    TabActions.event,
                    TabActions.menus,
                    TabActions.qanda,
                    TabActions.coc
                )
            ),
            bottomActionsUi = BottomActionsUi(
                actions = persistentListOf(
                    BottomActions.agenda,
                    BottomActions.speakers,
                    BottomActions.info
                )
            ),
            fabAction = FabActions.scanTicket,
            routeSelected = "agenda"
        ) {}
    }
}
