package com.paligot.confily.core.speakers.entities

import com.paligot.confily.core.schedules.entities.SessionItem
import com.paligot.confily.core.schedules.entities.mapToUi
import com.paligot.confily.models.ui.SpeakerUi
import com.paligot.confily.resources.Strings
import kotlinx.collections.immutable.toImmutableList
import kotlin.native.ObjCName

@ObjCName("SpeakerEntity")
class Speaker(
    val info: SpeakerInfo,
    val sessions: List<SessionItem>
)

fun Speaker.mapToUi(strings: Strings): SpeakerUi = SpeakerUi(
    url = info.photoUrl,
    name = info.displayName,
    pronouns = info.pronouns,
    jobTitle = info.jobTitle,
    company = info.company,
    activity = info.displayActivity(strings),
    bio = info.bio,
    websiteUrl = info.socials.firstOrNull { it.type == "website" }?.url,
    twitterUrl = info.socials.firstOrNull { it.type == "twitter" }?.url,
    mastodonUrl = info.socials.firstOrNull { it.type == "mastodon" }?.url,
    githubUrl = info.socials.firstOrNull { it.type == "github" }?.url,
    linkedinUrl = info.socials.firstOrNull { it.type == "linkedin" }?.url,
    talks = sessions.map { it.mapToUi(strings) }.toImmutableList()
)
