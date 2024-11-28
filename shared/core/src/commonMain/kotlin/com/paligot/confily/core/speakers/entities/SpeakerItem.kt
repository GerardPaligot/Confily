package com.paligot.confily.core.speakers.entities

import com.paligot.confily.models.ui.SpeakerItemUi
import com.paligot.confily.resources.Strings
import kotlin.native.ObjCName

@ObjCName("SpeakerItemEntity")
class SpeakerItem(
    val id: String,
    val displayName: String,
    val photoUrl: String,
    val jobTitle: String?,
    val company: String?
)

fun SpeakerItem.mapToUi(strings: Strings) = SpeakerItemUi(
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
