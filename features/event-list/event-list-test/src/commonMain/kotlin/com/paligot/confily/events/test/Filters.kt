package com.paligot.confily.events.test

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasText

fun hasEventItem(name: String, date: String): SemanticsMatcher = hasText(name) and hasText(date)
