package org.gdglille.devfest.android.theme.m3.speakers.test.pom

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick

class SpeakersGridPOM(composeTestRule: ComposeTestRule) {
    private val title = composeTestRule.onNodeWithText("Speakers")
    private val scroller = composeTestRule.onNode(hasScrollAction())

    private fun findSpeakerItem(speakerName: String): SemanticsNodeInteraction {
        return scroller
            .onChildren()
            .filterToOne(hasText(speakerName) and hasClickAction())
    }

    fun clickSpeakerItem(speakerName: String) {
        findSpeakerItem(speakerName).performClick()
    }

    fun assertSpeakerScreenIsDisplayed() {
        title.assertIsDisplayed()
    }

    fun assertSpeakerItemIsDisplayed(speakerName: String) {
        findSpeakerItem(speakerName).assertIsDisplayed()
    }

    fun assertNbSpeakerItemDisplayed(nb: Int) {
        scroller.onChildren().assertCountEquals(nb)
    }
}
