package org.gdglille.devfest.android.theme.vitamin.ui.components.appbars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.decathlon.vitamin.compose.appbars.topbars.ActionItem
import com.decathlon.vitamin.compose.appbars.topbars.VitaminTopBars
import com.decathlon.vitamin.compose.appbars.topbars.icons.VitaminNavigationIconButtons
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.R
import com.decathlon.vitamin.compose.foundation.R as RVitamin

@Composable
fun TopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (VitaminNavigationIconButtons.() -> Unit)? = null,
    actions: ImmutableList<ActionItem> = persistentListOf()
) {
    VitaminTopBars.Primary(
        title = title,
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions
    )
}

@Preview
@Composable
fun TopAppBarPreview() {
    Conferences4HallTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            TopAppBar(
                title = "Speakers"
            )
            TopAppBar(
                title = "Speakers",
                navigationIcon = { Context(onClick = { }, contentDescription = null) }
            )
            TopAppBar(
                title = "QrCode Scanner",
                navigationIcon = { Context(onClick = { }, contentDescription = null) },
                actions = persistentListOf(
                    ActionItem(
                        icon = painterResource(id = RVitamin.drawable.ic_vtmn_fullscreen_line),
                        contentDescription = stringResource(id = R.string.action_qrcode_scanner),
                        onClick = {}
                    )
                )
            )
        }
    }
}
