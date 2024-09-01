package org.gdglille.devfest.theme.m3.style.networking

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.m3.style.toColor
import org.gdglille.devfest.android.theme.m3.style.toDp
import org.gdglille.devfest.android.theme.m3.style.toTextStyle

object UserItemDefaults {
    val contentColor: Color
        @Composable
        get() = UserItemTokens.ContentColor.toColor()

    val contentPadding: PaddingValues
        @Composable
        get() = PaddingValues(
            horizontal = UserItemTokens.ContentHorizontalPadding.toDp(),
            vertical = UserItemTokens.ContentVerticalPadding.toDp()
        )

    val nameTextStyle: TextStyle
        @Composable
        get() = UserItemTokens.NameTextStyle.toTextStyle()

    val metaTextStyle: TextStyle
        @Composable
        get() = UserItemTokens.MetaTextStyle.toTextStyle()

    val iconSize: Dp = 16.dp
}
