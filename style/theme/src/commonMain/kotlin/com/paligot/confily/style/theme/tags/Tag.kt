package com.paligot.confily.style.theme.tags

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle

@Composable
fun SmallTag(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    colors: TagColors = TagDefaults.amethystColors(),
    style: TextStyle = TagDefaults.textStyle,
    shape: Shape = TagDefaults.smallShape
) {
    Row(
        modifier = modifier
            .background(color = colors.containerColor, shape = shape)
            .padding(paddingValues = TagDefaults.smallContentPadding),
        horizontalArrangement = Arrangement.spacedBy(TagDefaults.smallTextPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colors.contentColor,
                modifier = Modifier.size(TagDefaults.iconSize)
            )
        }
        Text(
            text = text,
            color = colors.contentColor,
            style = style
        )
    }
}

@Composable
fun MediumTag(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    colors: TagColors = TagDefaults.amethystColors(),
    style: TextStyle = TagDefaults.textStyle,
    shape: Shape = TagDefaults.mediumShape
) {
    Row(
        modifier = modifier
            .background(color = colors.containerColor, shape = shape)
            .padding(paddingValues = TagDefaults.mediumContentPadding),
        horizontalArrangement = Arrangement.spacedBy(TagDefaults.mediumTextPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colors.contentColor,
                modifier = Modifier.size(TagDefaults.iconSize)
            )
        }
        Text(
            text = text,
            color = colors.contentColor,
            style = style
        )
    }
}
