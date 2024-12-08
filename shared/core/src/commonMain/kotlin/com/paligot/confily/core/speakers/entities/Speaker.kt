package com.paligot.confily.core.speakers.entities

import com.paligot.confily.core.schedules.entities.SessionItem
import com.paligot.confily.core.schedules.entities.mapToTalkItemUi
import com.paligot.confily.core.socials.entities.Social
import com.paligot.confily.core.socials.entities.mapToSocialUi
import com.paligot.confily.models.ui.SpeakerUi
import com.paligot.confily.resources.Strings
import kotlinx.collections.immutable.toImmutableList
import kotlin.native.ObjCName

@ObjCName("SpeakerEntity")
class Speaker(
    val info: SpeakerInfo,
    val sessions: List<SessionItem>,
    val socials: List<Social>
)

fun Speaker.mapToSpeakerUi(strings: Strings): SpeakerUi = SpeakerUi(
    url = info.photoUrl,
    name = info.displayName,
    pronouns = info.pronouns,
    jobTitle = info.jobTitle,
    company = info.company,
    activity = info.displayActivity(strings),
    bio = info.bio,
    socials = socials.map(Social::mapToSocialUi).toImmutableList(),
    talks = sessions.map { it.mapToTalkItemUi(strings) }.toImmutableList()
)
