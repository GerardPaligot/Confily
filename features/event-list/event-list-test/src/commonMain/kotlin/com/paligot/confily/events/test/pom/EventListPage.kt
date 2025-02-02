package com.paligot.confily.events.test.pom

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onChildren
import com.paligot.confily.events.semantics.EventListSemantics
import com.paligot.confily.events.test.hasEventItem

class EventListPage(private val composeTestRule: ComposeTestRule) {
    fun findTabBy(label: String): SemanticsNodeInteraction = composeTestRule
        .onNode(hasText(label) and hasClickAction())

    fun findPagerContainer(): SemanticsNodeInteraction =
        composeTestRule.onNode(hasTestTag(EventListSemantics.pager))

    fun findEventItemContainer(): SemanticsNodeInteractionCollection =
        composeTestRule.onNode(hasTestTag(EventListSemantics.list)).onChildren()

    fun filterEventItem(name: String, date: String): SemanticsNodeInteractionCollection =
        findEventItemContainer().filter(hasEventItem(name, date))
}
