package org.gdglille.devfest.android.ui.m2.components.structure

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.gdglille.devfest.android.ui.m2.R
import org.gdglille.devfest.android.ui.m2.TopActions
import org.gdglille.devfest.android.ui.m2.components.appbars.BottomAppBar
import org.gdglille.devfest.android.ui.m2.components.appbars.TopAppBar
import org.gdglille.devfest.android.ui.m2.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.actions.BottomAction
import org.gdglille.devfest.android.ui.resources.actions.TopAction
import org.gdglille.devfest.android.ui.resources.models.BottomActionsUi
import org.gdglille.devfest.android.ui.resources.models.TopActionsUi

@Composable
fun Scaffold(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    topActions: TopActionsUi = TopActionsUi(),
    bottomActions: BottomActionsUi = BottomActionsUi(),
    routeSelected: String? = null,
    onTopActionClicked: (TopAction) -> Unit = {},
    onBottomActionClicked: (BottomAction) -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = stringResource(title),
                actions = topActions,
                onActionClicked = onTopActionClicked
            )
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
                topActions = listOf(
                    TopActions.share
                )
            ),
            bottomActions = BottomActionsUi(
                listOf()
            ),
            routeSelected = "agenda",
            content = {}
        )
    }
}