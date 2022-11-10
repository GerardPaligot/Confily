package org.gdglille.devfest.android.components.tags

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.Conferences4HallTheme

@Composable
fun Tag(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    colors: TagColors = TagDefaults.amethystColors(),
    style: TextStyle = TagDefaults.textStyle,
    shape: Shape = TagDefaults.shape
) {
    Row(
        modifier = modifier
            .background(color = colors.containerColor, shape = shape)
            .padding(paddingValues = TagDefaults.ContentPadding),
        horizontalArrangement = Arrangement.spacedBy(TagDefaults.IconTextPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colors.contentColor,
                modifier = Modifier.size(TagDefaults.IconSize)
            )
        }
        Text(
            text = text,
            color = colors.contentColor,
            style = style
        )
    }
}

@Suppress("LongMethod")
@Preview
@Composable
fun TagPreview() {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Conferences4HallTheme {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(horizontal = 4.dp)
            ) {
                Tag(
                    text = "Mobile",
                    icon = Icons.Outlined.PhoneAndroid
                )
                Tag(
                    text = "Mobile",
                    icon = Icons.Outlined.PhoneAndroid,
                    colors = TagDefaults.brickColors()
                )
                Tag(
                    text = "Mobile",
                    icon = Icons.Outlined.PhoneAndroid,
                    colors = TagDefaults.cobaltColors()
                )
                Tag(
                    text = "Mobile",
                    icon = Icons.Outlined.PhoneAndroid,
                    colors = TagDefaults.emeraldColors()
                )
                Tag(
                    text = "Mobile",
                    icon = Icons.Outlined.PhoneAndroid,
                    colors = TagDefaults.goldColors()
                )
                Tag(
                    text = "Mobile",
                    icon = Icons.Outlined.PhoneAndroid,
                    colors = TagDefaults.gravelColors()
                )
                Tag(
                    text = "Mobile",
                    icon = Icons.Outlined.PhoneAndroid,
                    colors = TagDefaults.jadeColors()
                )
                Tag(
                    text = "Mobile",
                    icon = Icons.Outlined.PhoneAndroid,
                    colors = TagDefaults.saffronColors()
                )
            }
        }
        Conferences4HallTheme(useDarkTheme = true) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(horizontal = 4.dp)
            ) {
                Tag(
                    text = "Mobile",
                    icon = Icons.Outlined.PhoneAndroid
                )
                Tag(
                    text = "Mobile",
                    icon = Icons.Outlined.PhoneAndroid,
                    colors = TagDefaults.brickColors()
                )
                Tag(
                    text = "Mobile",
                    icon = Icons.Outlined.PhoneAndroid,
                    colors = TagDefaults.cobaltColors()
                )
                Tag(
                    text = "Mobile",
                    icon = Icons.Outlined.PhoneAndroid,
                    colors = TagDefaults.emeraldColors()
                )
                Tag(
                    text = "Mobile",
                    icon = Icons.Outlined.PhoneAndroid,
                    colors = TagDefaults.goldColors()
                )
                Tag(
                    text = "Mobile",
                    icon = Icons.Outlined.PhoneAndroid,
                    colors = TagDefaults.gravelColors()
                )
                Tag(
                    text = "Mobile",
                    icon = Icons.Outlined.PhoneAndroid,
                    colors = TagDefaults.jadeColors()
                )
                Tag(
                    text = "Mobile",
                    icon = Icons.Outlined.PhoneAndroid,
                    colors = TagDefaults.saffronColors()
                )
            }
        }
    }
}
