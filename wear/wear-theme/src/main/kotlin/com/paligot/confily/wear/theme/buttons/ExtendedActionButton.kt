package com.paligot.confily.wear.theme.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.FilledTonalButton

@Composable
fun ExtendedActionButton(
    painter: Painter,
    label: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    secondaryLabel: (@Composable RowScope.() -> Unit)? = null,
    onClick: () -> Unit
) = ExtendedActionButton(
    image = { IconActionButton(painter = painter) },
    label = label,
    secondaryLabel = secondaryLabel,
    onClick = onClick,
    modifier = modifier
)

@Composable
fun ExtendedActionButton(
    image: @Composable BoxScope.() -> Unit,
    label: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    secondaryLabel: (@Composable RowScope.() -> Unit)? = null,
    onClick: () -> Unit
) = FilledTonalButton(
    icon = image,
    label = label,
    secondaryLabel = secondaryLabel,
    onClick = onClick,
    modifier = modifier.fillMaxWidth()
)

@Composable
fun IconActionButton(
    painter: Painter,
    modifier: Modifier = Modifier,
    background: Color = Color.Transparent,
    shape: Shape = CircleShape
) = Image(
    painter = painter,
    contentDescription = null,
    modifier = modifier.size(40.dp).clip(shape).background(background)
)
