package com.paligot.confily.style.theme.tags

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle

@Composable
fun SmallAutoColoredTag(
    text: String,
    colorName: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    style: TextStyle = TagDefaults.textStyle,
    shape: Shape = TagDefaults.mediumShape
) {
    SmallTag(
        text = text,
        modifier = modifier,
        icon = icon,
        colors = TagDefaults.colors(colorName),
        style = style,
        shape = shape
    )
}

@Composable
fun MediumAutoColoredTag(
    text: String,
    colorName: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    style: TextStyle = TagDefaults.textStyle,
    shape: Shape = TagDefaults.mediumShape
) {
    MediumTag(
        text = text,
        modifier = modifier,
        icon = icon,
        colors = TagDefaults.colors(colorName),
        style = style,
        shape = shape
    )
}
