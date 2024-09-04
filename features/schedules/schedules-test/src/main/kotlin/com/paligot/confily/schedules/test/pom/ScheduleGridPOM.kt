package com.paligot.confily.schedules.test.pom

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasScrollToIndexAction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick

class ScheduleGridPOM(private val composeTestRule: ComposeTestRule) {
    private val filtersIcon: SemanticsNodeInteraction =
        composeTestRule.onNodeWithContentDescription("Open filter screen")

    private fun findSessionItem(
        sessionName: String,
        speakers: List<String>,
        room: String,
        duration: Int,
        categoryName: String
    ): SemanticsNodeInteraction = composeTestRule.onNodeWithContentDescription(
        sessionContentDescription(sessionName, speakers, room, duration, categoryName)
    )

    private fun findFavoriteIcon(
        sessionName: String,
        speakers: List<String>,
        room: String,
        duration: Int,
        categoryName: String
    ): SemanticsNodeInteraction = composeTestRule.onNode(
        hasContentDescription("Add schedule in favorite") and hasParent(
            hasContentDescription(
                sessionContentDescription(sessionName, speakers, room, duration, categoryName)
            )
        )
    )

    fun clickOnFavoriteIcon(
        sessionName: String,
        speakers: List<String>,
        room: String,
        duration: Int,
        categoryName: String
    ): SemanticsNodeInteraction =
        findFavoriteIcon(sessionName, speakers, room, duration, categoryName).performClick()

    fun clickOnFiltersIcon(): SemanticsNodeInteraction = filtersIcon.performClick()

    fun clickOnFirstSessionItem() {
        composeTestRule
            .onNode(hasScrollToIndexAction() and hasParent(hasScrollToIndexAction()))
            .onChildren()
            .filter(hasClickAction())
            .onFirst()
            .performClick()
    }

    fun hasSessionItem(
        sessionName: String,
        speakers: List<String>,
        room: String,
        duration: Int,
        categoryName: String
    ): SemanticsNodeInteraction =
        findSessionItem(sessionName, speakers, room, duration, categoryName).assertIsDisplayed()

    fun hasNotSessionItem(
        sessionName: String,
        speakers: List<String>,
        room: String,
        duration: Int,
        categoryName: String
    ): SemanticsNodeInteraction =
        findSessionItem(sessionName, speakers, room, duration, categoryName).assertIsNotDisplayed()

    private fun sessionContentDescription(
        sessionName: String,
        speakers: List<String>,
        room: String,
        duration: Int,
        categoryName: String
    ): String {
        return "$sessionName with ${speakers.joinToString(", ")} as speaker " +
            "in $room room during $duration minutes in category $categoryName "
    }
}
