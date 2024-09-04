package com.paligot.confily.speakers.test.scopes

interface SpeakersGridRobotScope {
    fun clickSpeakerItem(
        speakerName: String,
        block: SpeakerDetailRobotScope.() -> Unit
    ): SpeakerDetailRobotScope

    fun assertSpeakerScreenIsDisplayed()
    fun assertSpeakerItemsIsDisplayed(speakers: List<String>)
}
