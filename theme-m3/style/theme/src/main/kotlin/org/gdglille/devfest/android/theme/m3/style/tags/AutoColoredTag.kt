package org.gdglille.devfest.android.theme.m3.style.tags

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle

@Composable
fun AutoColoredTag(
    text: String,
    colorName: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    style: TextStyle = TagDefaults.textStyle,
    shape: Shape = TagDefaults.shape
) {
    Tag(
        text = text,
        modifier = modifier,
        icon = icon,
        colors = TagDefaults.colors(colorName),
        style = style,
        shape = shape
    )
}
