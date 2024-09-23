package com.paligot.confily.wear.presentation.theme.tags

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Autorenew
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.Computer
import androidx.compose.material.icons.outlined.DataObject
import androidx.compose.material.icons.outlined.Draw
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text

@Composable
fun Tag(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    style: TextStyle = MaterialTheme.typography.bodySmall,
    shape: Shape = MaterialTheme.shapes.small
) {
    Row(
        modifier = modifier
            .background(color = containerColor, shape = shape)
            .padding(paddingValues = PaddingValues(4.dp)),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(style.fontSize.value.dp)
            )
        }
        Text(
            text = text,
            color = contentColor,
            style = style,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun String.findCategoryImageVector(): ImageVector? = when (this) {
    "database" -> Icons.Outlined.DataObject
    "computer" -> Icons.Outlined.Computer
    "public" -> Icons.Outlined.Public
    "cloud" -> Icons.Outlined.Cloud
    "smartphone" -> Icons.Outlined.Smartphone
    "lock" -> Icons.Outlined.Lock
    "autorenew" -> Icons.Outlined.Autorenew
    "psychology" -> Icons.Outlined.Psychology
    "draw" -> Icons.Outlined.Draw
    "language" -> Icons.Outlined.Language
    else -> null
}
