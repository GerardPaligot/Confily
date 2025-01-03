package com.paligot.confily.core.speakers.entities

import com.paligot.confily.resources.Strings
import com.paligot.confily.speakers.ui.models.SpeakerItemUi
import kotlin.native.ObjCName

@ObjCName("SpeakerItemEntity")
class SpeakerItem(
    val id: String,
    val displayName: String,
    val photoUrl: String,
    val jobTitle: String?,
    val company: String?
)

fun SpeakerItem.mapToSpeakerItemUi(strings: Strings) = SpeakerItemUi(
    id = id,
    name = displayName,
    activity = displayActivity(strings) ?: "",
    url = photoUrl
)

fun SpeakerItem.displayActivity(strings: Strings) = when {
    jobTitle != null && company != null -> strings.texts.speakerActivity(jobTitle, company)
    jobTitle == null && company != null -> company
    jobTitle != null && company == null -> jobTitle
    else -> null
}
