package org.gdglille.devfest.android.theme.m3.schedules.test.pom

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.paligot.confily.core.test.patterns.withRole

class ScheduleDetailsPOM(private val composeTestRule: ComposeTestRule) {
    private fun findMetadata(title: String, room: String, time: Int, category: String) =
        composeTestRule.onNodeWithContentDescription(
            "$title  in $room room during $time minutes in category $category "
        )

    private fun findAbstract(abstract: String) =
        composeTestRule.onNodeWithContentDescription(abstract)

    private fun findSpeaker(speaker: String) =
        composeTestRule.onNode(hasText(speaker))

    private val backIcon = composeTestRule.onNode(
        withRole(Role.Button) and hasContentDescription("Back")
    )

    fun assertMetadata(title: String, room: String, time: Int, category: String) {
        findMetadata(title, room, time, category).assertIsDisplayed()
    }

    fun assertAbstract(abstract: String) {
        findAbstract(abstract).assertIsDisplayed()
    }

    fun assertSpeakers(speakers: List<String>) {
        speakers.forEach { findSpeaker(it).assertIsDisplayed() }
    }

    fun back() {
        backIcon.assertIsDisplayed().performClick()
    }
}
