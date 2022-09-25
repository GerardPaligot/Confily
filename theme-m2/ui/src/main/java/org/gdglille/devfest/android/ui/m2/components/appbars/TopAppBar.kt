package org.gdglille.devfest.android.ui.m2.components.appbars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.ui.m2.R
import org.gdglille.devfest.android.ui.m2.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.actions.TopAction
import org.gdglille.devfest.android.ui.resources.models.TopActionsUi

@Composable
fun TopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (AppBarIcons.() -> Unit)? = null,
    actions: TopActionsUi = TopActionsUi(),
    onActionClicked: ((TopAction) -> Unit)? = null
) {
    androidx.compose.material.TopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
        navigationIcon = navigationIcon.takeOrNull(),
        actions = {
            actions.topActions.forEach { action ->
                IconButton(onClick = { onActionClicked?.let { it(action) } }) {
                    Icon(
                        painter = painterResource(action.icon),
                        contentDescription = action.contentDescription?.let { stringResource(id = it) }
                    )
                }
            }
        }
    )
}

internal fun (@Composable AppBarIcons.() -> Unit)?.takeOrNull(): (@Composable () -> Unit)? {
    if (this == null) return null
    return {
        AppBarIcons.this()
    }
}

@Preview
@Composable
fun TopAppBarPeview() {
    Conferences4HallTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            TopAppBar(
                title = "Speakers"
            )
            TopAppBar(
                title = "Speakers",
                navigationIcon = { Back { } }
            )
            TopAppBar(
                title = "QrCode Scanner",
                navigationIcon = { Back { } },
                actions = TopActionsUi(
                    topActions = arrayListOf(
                        TopAction(
                            id = 0,
                            icon = R.drawable.ic_mtrl_qr_code_scanner_line,
                            contentDescription = R.string.action_qrcode_scanner
                        )
                    )
                )
            )
        }
    }
}
