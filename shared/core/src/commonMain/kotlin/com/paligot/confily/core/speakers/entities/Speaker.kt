package com.paligot.confily.core.speakers.entities

import com.paligot.confily.core.schedules.entities.SessionItem
import com.paligot.confily.core.schedules.entities.mapToTalkItemUi
import com.paligot.confily.core.socials.entities.Social
import com.paligot.confily.core.socials.entities.mapToSocialUi
import com.paligot.confily.resources.Strings
import com.paligot.confily.speakers.panes.models.SpeakerUi
import com.paligot.confily.speakers.ui.models.SpeakerInfoUi
import kotlinx.collections.immutable.toImmutableList
import kotlin.native.ObjCName

@ObjCName("SpeakerEntity")
class Speaker(
    val info: SpeakerInfo,
    val sessions: List<SessionItem>,
    val socials: List<Social>
)

fun Speaker.mapToSpeakerUi(strings: Strings): SpeakerUi = SpeakerUi(
    info = SpeakerInfoUi(
        url = info.photoUrl,
        name = info.displayName,
        pronouns = info.pronouns,
        company = info.company,
        jobTitle = info.jobTitle,
        activity = info.displayActivity(strings),
        bio = info.bio,
        socials = socials.map(Social::mapToSocialUi).toImmutableList()
    ),
    talks = sessions.map { it.mapToTalkItemUi(strings) }.toImmutableList()
)
