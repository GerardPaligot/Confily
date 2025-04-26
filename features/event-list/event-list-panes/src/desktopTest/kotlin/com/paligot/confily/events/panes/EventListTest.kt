package com.paligot.confily.events.panes

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performScrollToIndex
import com.paligot.confily.events.test.pom.EventListPage
import com.paligot.confily.events.ui.models.EventItemListUi
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.screen_events_future
import com.paligot.confily.resources.screen_events_past
import org.jetbrains.compose.resources.stringResource
import org.junit.Rule
import org.junit.Test

class EventListTest {
    @get:Rule
    val rule = createComposeRule()

    private val page = EventListPage(rule)

    /**
     * Given a list of future events
     * When the user is on the event list screen
     * Then the user should see the future events
     */
    @Test
    fun shouldDisplayFutureEventItems() {
        val firstEventItem = EventItemListUi.fake.future.first()
        var tabFuture = ""
        var tabPast = ""
        rule.setContent {
            tabFuture = stringResource(Resource.string.screen_events_future)
            tabPast = stringResource(Resource.string.screen_events_past)
            EventListPane(
                events = EventItemListUi.fake,
                onEventClicked = {}
            )
        }
        page.findTabBy(tabFuture).assertIsDisplayed()
        page.findTabBy(tabPast).assertIsDisplayed()
        page.findEventItemContainer().assertCountEquals(1)
        page.filterEventItem(firstEventItem.name, firstEventItem.date)
            .assertCountEquals(1)
            .onFirst()
            .assertIsDisplayed()
    }

    /**
     * Given a list of events with future and past events selectable from tab menu
     * When the user is on the event list screen
     * Then the user should be able to change between future and past events
     */
    @Test
    fun shouldChangeTabBetweenFutureAndPastEvents() {
        var tabFuture = ""
        var tabPast = ""
        rule.setContent {
            tabFuture = stringResource(Resource.string.screen_events_future)
            tabPast = stringResource(Resource.string.screen_events_past)
            EventListPane(events = EventItemListUi.fake, onEventClicked = {})
        }
        page.findTabBy(tabFuture).assertIsSelected()
        page.findTabBy(tabPast).assertIsNotSelected()
        page.findPagerContainer().performScrollToIndex(1)
        page.findTabBy(tabFuture).assertIsNotSelected()
        page.findTabBy(tabPast).assertIsSelected()
    }

    /**
     * Given a list of future events
     * When the user is on the event list screen
     * Then the user should see the loading indicator but the semantic tree should not contain any event items
     */
    @Test
    fun shouldRemoveEventItemsDuringLoadingMode() {
        val firstEventItem = EventItemListUi.fake.future.first()
        rule.setContent {
            EventListPane(events = EventItemListUi.fake, onEventClicked = {}, isLoading = true)
        }
        page.findEventItemContainer().assertCountEquals(1)
        page.filterEventItem(firstEventItem.name, firstEventItem.date).assertCountEquals(0)
    }
}
