package com.paligot.confily.events.ui

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import com.paligot.confily.events.test.onEventItemNode
import com.paligot.confily.events.ui.models.EventItemUi
import org.junit.Rule
import org.junit.Test

class EventItemTest {
    @get:Rule
    val rule = createComposeRule()

    /**
     * Given an event item ui with a name and a date
     * When we compose EventItem component
     * Then we should find name and date in the component tree
     */
    @Test
    fun shouldDisplayEventNameAndDateInEventItem() {
        rule.setContent {
            EventItem(item = EventItemUi.fake, onClick = {})
        }
        rule.onEventItemNode(EventItemUi.fake.name, EventItemUi.fake.date)
            .assertHasClickAction()
            .assertIsDisplayed()
    }

    /**
     * Given an event item ui in loading mode
     * When we compose EventItem component
     * Then we should have an empty semantic tree
     */
    @Test
    fun shouldClearSemanticTreeDuringLoadingState() {
        rule.setContent {
            EventItem(item = EventItemUi.fake, isLoading = true, onClick = {})
        }
        rule.onEventItemNode(EventItemUi.fake.name, EventItemUi.fake.date)
            .assertDoesNotExist()
    }
}
