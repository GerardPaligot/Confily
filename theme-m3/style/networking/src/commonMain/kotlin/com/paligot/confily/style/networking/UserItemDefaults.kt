package com.paligot.confily.style.networking

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.paligot.confily.style.theme.toColor
import com.paligot.confily.style.theme.toDp
import com.paligot.confily.style.theme.toTextStyle

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
