package com.paligot.confily.backend.formats.infrastructure.exposed

import com.paligot.confily.models.Format

fun FormatEntity.toModel(): Format = Format(
    id = this.id.value.toString(),
    name = this.name,
    time = 0
)
