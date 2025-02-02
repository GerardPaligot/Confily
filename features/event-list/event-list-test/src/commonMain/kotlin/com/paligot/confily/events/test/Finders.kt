package com.paligot.confily.events.test

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider

fun SemanticsNodeInteractionsProvider.onEventItemNode(
    name: String,
    date: String,
    useUnmergedTree: Boolean = false
): SemanticsNodeInteraction = onNode(matcher = hasEventItem(name, date), useUnmergedTree)
