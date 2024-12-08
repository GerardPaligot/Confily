package com.paligot.confily.core.schedules.entities

import com.paligot.confily.resources.Strings

enum class Level {
    Beginner,
    Intermediate,
    Advanced
}

fun Level?.mapToLocalizedString(strings: Strings): String? = when (this) {
    Level.Advanced -> strings.texts.levelAdvanced
    Level.Intermediate -> strings.texts.levelIntermediate
    Level.Beginner -> strings.texts.levelBeginner
    else -> null
}
