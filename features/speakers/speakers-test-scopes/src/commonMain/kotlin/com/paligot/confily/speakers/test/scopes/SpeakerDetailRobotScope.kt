package com.paligot.confily.speakers.test.scopes

interface SpeakerDetailRobotScope {
    fun assertSpeakerInfoAreDisplayed(name: String, company: String?, bio: String)
    fun assertSpeakerTalkIsDisplayed(
        title: String,
        speakers: List<String>,
        room: String,
        time: Int,
        category: String
    )

    fun backToSpeakersScreen(block: SpeakersGridRobotScope.() -> Unit): SpeakersGridRobotScope
}
