package com.paligot.confily.map.editor.detail.presentation

import com.paligot.confily.models.MapPictogram
import com.paligot.confily.models.MapShape
import com.paligot.confily.models.MappingType
import com.paligot.confily.models.Offset
import com.paligot.confily.models.PictogramType
import com.paligot.confily.style.components.map.ui.models.OffsetUi
import com.paligot.confily.style.components.map.ui.models.PictogramUi
import com.paligot.confily.style.components.map.ui.models.ShapeUi
import androidx.compose.ui.geometry.Offset as ComposeOffset
import com.paligot.confily.style.components.map.ui.models.MappingType as UiMappingType
import com.paligot.confily.style.components.map.ui.models.PictogramType as UiPictogramType

internal fun Offset.toComposeOffset(): ComposeOffset = ComposeOffset(x, y)

internal fun ComposeOffset.toSerialOffset(): Offset = Offset(x, y)

internal fun Offset.toUiOffset(): OffsetUi = OffsetUi(x, y)

internal fun UiMappingType.toNetMappingType(): MappingType = MappingType.valueOf(name)

internal fun MappingType.toUiMappingType(): UiMappingType = UiMappingType.valueOf(name)

internal fun MapShape.toUiShape(): ShapeUi = ShapeUi(
    label = name,
    description = description,
    start = start.toUiOffset(),
    end = end.toUiOffset(),
    type = type.toUiMappingType()
)

internal fun UiPictogramType.toNetPictogramType(): PictogramType = PictogramType.valueOf(name)

internal fun PictogramType.toUiPictogramType(): UiPictogramType = UiPictogramType.valueOf(name)

internal fun MapPictogram.toUiPictogram(): PictogramUi = PictogramUi(
    label = name,
    description = description,
    position = position.toUiOffset(),
    type = type.toUiPictogramType()
)
