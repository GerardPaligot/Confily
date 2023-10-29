package org.gdglille.devfest.android.theme.m3.style.tags

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
import androidx.compose.ui.unit.dp
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.android.theme.m3.style.previews.ThemedPreviews

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

@Suppress("LongMethod", "UnusedPrivateMember")
@ThemedPreviews
@Composable
private fun MediumTagPreview() {
    Conferences4HallTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(4.dp)
        ) {
            MediumTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid
            )
            MediumTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid,
                colors = TagDefaults.brickColors()
            )
            MediumTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid,
                colors = TagDefaults.cobaltColors()
            )
            MediumTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid,
                colors = TagDefaults.emeraldColors()
            )
            MediumTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid,
                colors = TagDefaults.goldColors()
            )
            MediumTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid,
                colors = TagDefaults.gravelColors()
            )
            MediumTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid,
                colors = TagDefaults.jadeColors()
            )
            MediumTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid,
                colors = TagDefaults.saffronColors()
            )
        }
    }
}

@Suppress("LongMethod", "UnusedPrivateMember")
@ThemedPreviews
@Composable
private fun SmallTagPreview() {
    Conferences4HallTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(4.dp)
        ) {
            SmallTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid
            )
            SmallTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid,
                colors = TagDefaults.brickColors()
            )
            SmallTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid,
                colors = TagDefaults.cobaltColors()
            )
            SmallTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid,
                colors = TagDefaults.emeraldColors()
            )
            SmallTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid,
                colors = TagDefaults.goldColors()
            )
            SmallTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid,
                colors = TagDefaults.gravelColors()
            )
            SmallTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid,
                colors = TagDefaults.jadeColors()
            )
            SmallTag(
                text = "Mobile",
                icon = Icons.Outlined.PhoneAndroid,
                colors = TagDefaults.saffronColors()
            )
        }
    }
}
