package org.gdglille.devfest.android.theme.m3.speakers.test.scopes

interface SpeakersGridRobotScope {
    fun clickSpeakerItem(
        speakerName: String,
        block: SpeakerDetailRobotScope.() -> Unit
    ): SpeakerDetailRobotScope

    fun assertSpeakerScreenIsDisplayed()
    fun assertSpeakerItemsIsDisplayed(speakers: List<String>)
}
