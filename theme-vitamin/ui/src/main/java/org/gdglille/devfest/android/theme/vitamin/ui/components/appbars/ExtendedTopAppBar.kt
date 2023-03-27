package org.gdglille.devfest.android.theme.vitamin.ui.components.appbars

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import com.decathlon.vitamin.compose.foundation.R as RVitamin

@Composable
fun ExtendedTopAppBar(
    title: String,
    navigationIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = VitaminTheme.colors.vtmnBackgroundPrimary,
    contentColor: Color = VitaminTheme.colors.vtmnContentPrimary,
    elevation: Dp = AppBarDefaults.TopAppBarElevation,
    contentPadding: PaddingValues = AppBarDefaults.ContentPadding,
    shape: Shape = RectangleShape
) {
    Surface(
        color = backgroundColor,
        contentColor = contentColor,
        elevation = elevation,
        shape = shape,
        modifier = modifier
    ) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
            Column(
                Modifier.fillMaxWidth()
                    .padding(contentPadding)
                    .height(ExtendedAppBarHeight),
            ) {
                Row(IconModifier, verticalAlignment = Alignment.CenterVertically) {
                    navigationIcon()
                }
                Row(
                    modifier = Modifier.weight(1f).fillMaxWidth().padding(start = AppBarTitleStartPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = VitaminTheme.typography.h6
                    )
                }
            }
        }
    }
}

@Preview
@Composable
internal fun ExtendedTopAppBarPreview() {
    Conferences4HallTheme {
        ExtendedTopAppBar(
            title = "Create my profile",
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(RVitamin.drawable.ic_vtmn_arrow_left_line),
                        contentDescription = null
                    )
                }
            }
        )
    }
}

@Preview
@Composable
internal fun ExtendedTopAppBarContextualPreview() {
    Conferences4HallTheme {
        ExtendedTopAppBar(
            title = "Create my profile",
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(RVitamin.drawable.ic_vtmn_arrow_left_line),
                        contentDescription = null
                    )
                }
            },
            backgroundColor = VitaminTheme.colors.vtmnBackgroundBrandPrimary,
            contentColor = VitaminTheme.colors.vtmnContentPrimaryReversed
        )
    }
}

private val AppBarHeight = 56.dp
private val ExtendedAppBarHeight = 124.dp
private val AppBarHorizontalPadding = 4.dp
private val AppBarTitleStartPadding = 72.dp
private val IconModifier = Modifier.height(AppBarHeight).width(AppBarTitleStartPadding - AppBarHorizontalPadding)
