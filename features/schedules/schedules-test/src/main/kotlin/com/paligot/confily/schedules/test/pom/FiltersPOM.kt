package com.paligot.confily.schedules.test.pom

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.paligot.confily.core.test.patterns.withRole

class FiltersPOM(private val composeTestRule: ComposeTestRule) {
    private val filterTitleScreen = composeTestRule.onNodeWithText("Filters")
    private val favoritesOnly = composeTestRule.onNode(
        withRole(Role.Checkbox) and hasText("Only favorites schedules")
    )
    private val backIcon = composeTestRule.onNode(
        withRole(Role.Button) and hasContentDescription("Back")
    )

    private fun findCategory(categoryName: String) =
        composeTestRule.onNode(withRole(Role.Button) and hasText(categoryName))

    private fun findFormat(formatName: String) =
        composeTestRule.onNode(withRole(Role.Button) and hasText(formatName))

    fun clickOnFavoritesOnly(): FiltersPOM {
        favoritesOnly.performClick()
        return this
    }

    fun clickOnCategory(categoryName: String) {
        findCategory(categoryName).performClick()
    }

    fun clickOnFormat(formatName: String) {
        findFormat(formatName).performClick()
    }

    fun back() {
        backIcon.performClick()
    }

    fun assertFilterScreenIsOpened() {
        filterTitleScreen.assertIsDisplayed()
    }
}
