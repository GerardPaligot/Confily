package com.paligot.confily.core.speakers.entities

import com.paligot.confily.core.events.entities.Social
import com.paligot.confily.resources.Strings
import kotlin.native.ObjCName

@ObjCName("SpeakerInfoEntity")
class SpeakerInfo(
    val id: String,
    val displayName: String,
    val bio: String,
    val photoUrl: String,
    val pronouns: String?,
    val jobTitle: String?,
    val company: String?,
    val socials: List<Social>
)

internal fun SpeakerInfo.displayActivity(strings: Strings) = when {
    jobTitle != null && company != null -> strings.texts.speakerActivity(jobTitle, company)
    jobTitle == null && company != null -> company
    jobTitle != null && company == null -> jobTitle
    else -> null
}
